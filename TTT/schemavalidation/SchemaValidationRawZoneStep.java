package schemavalidation;

import common.SSHConnector;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.io.IOException;
import java.net.URL;
import java.sql.*;

import org.apache.hadoop.security.UserGroupInformation;

public class SchemaValidationRawZoneStep {
    private static SSHConnector ssh = new SSHConnector();

    @Given("^an Avro files$")
    public void setupAvroFiles()  throws SQLException, ClassNotFoundException, IOException {
        URL krbIni = SchemaValidationRawZoneStep.class.getClassLoader().getResource("krb5.ini");
        System.out.println("krb5 config:" + krbIni.getPath());
        System.setProperty("java.security.krb5.conf", krbIni.getPath());

        System.out.println("Setting UserGroupInformation and hadoop.security.authentication ... ");
        org.apache.hadoop.conf.Configuration conf = new org.apache.hadoop.conf.Configuration();
        conf.set("hadoop.security.authentication", "Kerberos");
        UserGroupInformation.setConfiguration(conf);
        URL keytabPath = SchemaValidationRawZoneStep.class.getClassLoader().getResource("dqerxhdpapp.keytab");
        UserGroupInformation.loginUserFromKeytab("dqerxhdpapp@RXDEV.HDP.WALGREENS.COM", keytabPath.getPath());

        System.out.println("Loading HIVE JDB driver ... ");
        Class.forName("org.apache.hive.jdbc.HiveDriver");
        final String CONN_STR = "jdbc:hive2://dld-awurxhd0102.walgreens.com:2181,dld-awurxhd0103.walgreens.com:2181,dld-awurxhd0104.walgreens.com:2181/;serviceDiscoveryMode=zooKeeper;zooKeeperNamespace=hiveserver2";
        System.out.println("Connecting using "+ CONN_STR +" ... ");
        Connection conn = DriverManager.getConnection(CONN_STR);

        System.out.println("connected. getting databases: ");

        PreparedStatement stmt = conn.prepareStatement("select * from dqerxref.location");
        ResultSet rs  = stmt.executeQuery();

        while(rs.next()) {
            System.out.println(rs.getString(1));
        }
        System.out.println("");
        System.out.println("done. ");
    }

    @When("^I query for schema \"([^\"]*)\"$")
    public void i_query_for_schema(String schemaFileName) {
        System.out.println("Implement Me");
    }

    @Then("^I can see corresponding columns names$")
    public void i_can_see_corresponding_columns_names() {
        System.out.println("Implement Me");
    }
}
