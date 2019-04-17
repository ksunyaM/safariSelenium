package schemavalidation.Connection;

import cucumber.api.java.After;
import cucumber.api.java.Before;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionToHive {
    private static String driverName = "org.apache.hive.jdbc.HiveDriver";
    // get connection
    public static java.sql.Connection con;
    public static Statement stmt;


    @Before("@SearchReport")
    public void setupConnectionToHive() throws SQLException,
            ClassNotFoundException {
        Class.forName(driverName);
        // Register driver and create driver instance
        con = DriverManager.getConnection("jdbc:hive2://localhost:10000/default", "admin", "admin123");
        stmt = con.createStatement();
    }

    @After("@SearchReport")
    public void dropConnectionToHive() throws SQLException {
        con.close();
    }
}
