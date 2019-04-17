package schemavalidation;

import common.SSHConnector;
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

public class WeeklyAggregateSparkJobStep {
    private static SSHConnector ssh = new SSHConnector();

    @Given("^data in the pub_scm_item_movement_trx$")
    public void data_in_the_pub_scm_item_movement_trx() {
        ssh.connect();
    }

    @When("^I run Weekly Spark Job$")
    public void i_run_Weekly_Spark_Job() {
        ssh.executeCommand("./j.sh");
    }

    @Then("^I can see corresponding data$")
    public void i_can_see_corresponding_data() throws ClassNotFoundException, IOException, SQLException {
        List<String> expectedTable = new ArrayList();
        //List<String> actualTable = table.asList(String.class);

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
        PreparedStatement stmtRepair = conn.prepareStatement("MSCK REPAIR TABLE dqe_sc_pub.pub_scm_item_movement_eow_snp");
        stmtRepair.execute();

        PreparedStatement stmt = conn.prepareStatement("select hard_allocated_adjustment_quantity from dqe_sc_pub.pub_scm_item_movement_eow_snp where location_product_code='21224^4-4' and report_version_number=0");
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            assertEquals("8.9", rs.getString("hard_allocated_adjustment_quantity"));
        }

        System.out.println("done. ");
    }

}
