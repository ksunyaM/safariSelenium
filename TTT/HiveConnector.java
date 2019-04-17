package common;

import org.apache.hadoop.security.UserGroupInformation;
import schemavalidation.SchemaValidationRawZoneStep;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class HiveConnector {

    public Connection connection;

    public Connection connectToHive()  throws SQLException, ClassNotFoundException, IOException {
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
            System.out.println("Connecting using " + CONN_STR + " ... ");
            connection = DriverManager.getConnection(CONN_STR);

            System.out.println("connected. getting databases: ");
            System.out.println("done. ");
            return connection;

    }
}
