/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.security.UserGroupInformation;

import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.*;

import static com.oneleo.test.automation.core.LogUtils.log;

public class HiveUtils {
    private static final Logger LOGGER = log(HiveUtils.class);
    private Map<String, Connection> connections = new HashMap<>();
    private static HiveUtils hive;

    private static String db = "default";
    private static String kerberos = "default";

    private HiveUtils() {
    }

    public HiveUtils connect() throws SQLException {
        setConnection();
        return this;
    }

    public HiveUtils connect(String dbResource) throws SQLException {
        db = dbResource;
        setConnection();
        return this;
    }

    public HiveUtils connect(String dbResource, String kerberosResource) throws SQLException {
        db = dbResource;
        kerberos = kerberosResource;
        setConnection();
        return this;
    }

    public static HiveUtils hive() {
        if (hive == null) {
            synchronized (HiveUtils.class) {
                hive = new HiveUtils();
            }
        }
        return hive;
    }

    private Connection setConnection() throws SQLException {
        if (!connections.keySet().contains(db) || connections.get(db).isClosed()) {
            org.apache.hadoop.conf.Configuration conf = new org.apache.hadoop.conf.Configuration();
            conf.set("hadoop.security.authentication", "Kerberos");
            UserGroupInformation.setConfiguration(conf);

            Properties propsKrb = PropertiesReader.read(kerberos + "-kerberos.properties", true);
            Properties propsDb = PropertiesReader.read(db + "-database.properties", true);

            String principal = propsKrb.getProperty("kerberos.principal");
            String basicPath = System.getProperty("user.dir") + File.separator + System.getProperty("confPath") + File.separator;
            String keytabFilePath = basicPath + StringUtils.substringBefore(principal, "@") + ".keytab";
            String confFilePath = basicPath + "krb5.ini";

            System.setProperty("java.security.krb5.conf", confFilePath);
            try {
                UserGroupInformation.loginUserFromKeytab(principal, keytabFilePath);
                Class.forName(propsDb.getProperty("jdbc.driverClassName"));
                connections.put(db, DriverManager.getConnection(propsDb.getProperty("jdbc.url")));
            } catch (IOException ex) {
                LOGGER.error("Unable to login with keytab in the path:\n %s", keytabFilePath);
                throw new RuntimeException(ex.toString());
            } catch (ClassNotFoundException ex) {
                LOGGER.error("Unable find driver class in the classpath");
                throw new RuntimeException(ex.toString());
            }
        }
        return connections.get(db);
    }


    public List<Map<String, Object>> runSQLForListRowsParameters(String query, Object... parameters) throws SQLException {
        LOGGER.info(String.format("Going to find by query: %s\n with parameters: %s", query, Arrays.toString(parameters)));
        PreparedStatement statement = connections.get(db).prepareStatement(query);
        if (parameters != null) {
            for (int i = 0; i < parameters.length; i++){
                statement.setObject( i + 1, parameters[i]);
            }
        }
        ResultSet resultSet = statement.executeQuery();
        ResultSetMetaData md  = resultSet.getMetaData();
        List<Map<String, Object>> res = new ArrayList<>();

        while (resultSet.next()){
            Map<String, Object> row = new HashMap<>();
            for(int i = 1; i <= md.getColumnCount(); i++){
                row.put(md.getColumnName(i), resultSet.getObject(i));
            }
            res.add(row);
        }
        LOGGER.info(String.format("For query:\n %s; with parameters: %s\n result size is: %s", query, Arrays.toString(parameters), res.size()));
        return res;
    }


    public void executeQuery(String query) throws SQLException {
        LOGGER.info(String.format("Going to find by query: %s\n ", query));
        PreparedStatement statement = connections.get(db).prepareStatement(query);
        statement.executeQuery();
    }
}
