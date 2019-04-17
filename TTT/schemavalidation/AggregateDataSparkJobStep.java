package schemavalidation;

import common.SSHConnector;
import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.apache.hadoop.security.UserGroupInformation;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class AggregateDataSparkJobStep {
    private static SSHConnector ssh = new SSHConnector();

    @Given("^data from all of the required directories$")
    public void data_from_all_of_the_required_directories() {
        ssh.connect();
       // ssh.sendCommand("./tab.sh");
        //ssh.sendCommand("spark-submit --class com.wba.stock.wip_to_curated.GenerateInputFiles --packages org.scala-lang:scala-library:2.11.8,com.typesafe:config:1.3.3 --master local[*] ./wip-to-curated-1.0-SNAPSHOT.jar" +
                //" \"1\" \"1\" ./spark.conf");
    }

    @When("^I run Aggregate Data Spark Job$")
    public void i_run_Aggregate_Data_Spark_Job() {
        //ssh.sendCommand("spark-submit --class com.wba.stock.wip_to_curated.WipToCurated --packages org.scala-lang:scala-library:2.11.8,com.typesafe:config:1.3.3 --master local[*] ./wip-to-curated-1.0-SNAPSHOT.jar " +
         //       "\"1\" \"1\" ./spark.conf");
    }

    @Then("^I can see crated data in the cur_scm_stock_trx table$")
    public void i_can_see_crated_data_in_the_cur_scm_stock_trx_table(DataTable table) throws ClassNotFoundException, IOException, SQLException {
        List<String> expectedTable = new ArrayList();
        List<String> actualTable = table.asList(String.class);

        URL krbIni = AggregateDataSparkJobStep.class.getClassLoader().getResource("krb5.ini");
        System.out.println("krb5 config:" + krbIni.getPath());
        System.setProperty("java.security.krb5.conf", krbIni.getPath());

        System.out.println("Setting UserGroupInformation and hadoop.security.authentication ... ");
        org.apache.hadoop.conf.Configuration conf = new org.apache.hadoop.conf.Configuration();
        conf.set("hadoop.security.authentication", "Kerberos");
        UserGroupInformation.setConfiguration(conf);
        URL keytabPath = AggregateDataSparkJobStep.class.getClassLoader().getResource("dqerxhdpapp.keytab");
        UserGroupInformation.loginUserFromKeytab("dqerxhdpapp@RXDEV.HDP.WALGREENS.COM", keytabPath.getPath());

        System.out.println("Loading HIVE JDB driver ... ");
        Class.forName("org.apache.hive.jdbc.HiveDriver");
        final String CONN_STR = "jdbc:hive2://dld-awurxhd0102.walgreens.com:2181,dld-awurxhd0103.walgreens.com:2181,dld-awurxhd0104.walgreens.com:2181/;serviceDiscoveryMode=zooKeeper;zooKeeperNamespace=hiveserver2";
        System.out.println("Connecting using " + CONN_STR + " ... ");
        Connection conn = DriverManager.getConnection(CONN_STR);

        System.out.println("connected. getting databases: ");
        PreparedStatement stmtRepair = conn.prepareStatement("MSCK REPAIR TABLE dqe_sc_prep.cur_scm_stock_trx");
        stmtRepair.execute();

        PreparedStatement stmt = conn.prepareStatement("select transaction_date from  dqe_Sc_prep.cur_scm_stock_trx");
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            expectedTable.add(rs.getString(1));
        }

        assertEquals(expectedTable, actualTable);

        System.out.println("");
        System.out.println("done. ");
    }
}
