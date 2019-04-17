/*
 * Copyright 2018 Walgreen Co.
 */
package com.wba.test.utils.tdm;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.wba.test.utils.ResourceUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public enum DBConnection {
    CONNECT;

    private StoreConnection storeConnection;
    private int timeout = 60000;
    private Logger LOGGER = LoggerFactory.getLogger(DBConnection.class);
    private Map<String, Connection> connectionMap = new HashMap<>();
    private Map<String, Session> sessionMap = new HashMap<>();

    public Connection connectToStoreDB(String storeNumber) {
        getConnectionDetails();
        if (!connectionMap.keySet().contains(storeNumber)) {
            String username = storeConnection.getStoreDB().get(storeNumber) == null
                    ? storeConnection.getStoreDB().get("default").get("username")
                    : storeConnection.getStoreDB().get(storeNumber).get("username");
            String password = storeConnection.getStoreDB().get(storeNumber) == null
                    ? storeConnection.getStoreDB().get("default").get("password")
                    : storeConnection.getStoreDB().get(storeNumber).get("password");
            try {
                Connection conn = DriverManager.getConnection(storeConnection.getHostStoreDB()
                        .replace("storeNumber", storeNumber)
                        .replace("username", username)
                        .replace("password", password));
                connectionMap.put(storeNumber, conn);
            } catch (Exception e) {
                throw new RuntimeException("Store " + storeNumber + "'s database can not be connected to right now. This may be an issue with the store, but it's possible this is an issue of you not having extended VPN access");
            }
        }
        return connectionMap.get(storeNumber);
    }

    //gives a connection to central (need to be connected to VPN to do this)
    public Connection connectToCentralDB() {
        getConnectionDetails();
        if (!connectionMap.keySet().contains("central")) {
            try {
                Connection conn = DriverManager.getConnection(storeConnection.getHostCentralDB(),
                        storeConnection.getCentralDB().get("username"),
                        storeConnection.getCentralDB().get("password"));
                connectionMap.put("central", conn);
            } catch (Exception e) {
                throw new RuntimeException("You computer cannot connect to the central database right now. Ensure you are connected to the VPN");
            }
        }
        return connectionMap.get("central");
    }

    public Session getStoreSession(String storeNumber) {
        JSch jSch = new JSch();
        getConnectionDetails();
        try {
            if (!sessionMap.containsKey(storeNumber) || !sessionMap.get(storeNumber).isConnected()) {
                Session session = jSch.getSession(storeConnection.getStoreSSH().get(storeNumber) == null
                                ? storeConnection.getStoreSSH().get("default").get("username")
                                : storeConnection.getStoreSSH().get(storeNumber).get("username"),
                        storeConnection.getHostStoreSSH().replace("storeNumber", storeNumber), 22);
                session.setConfig("StrictHostKeyChecking", "no");
                session.setPassword(storeConnection.getStoreSSH().get(storeNumber) == null
                        ? storeConnection.getStoreSSH().get("default").get("password")
                        : storeConnection.getStoreSSH().get(storeNumber).get("password"));
                session.setConfig("StrictHostKeyChecking", "no");
                session.connect(timeout);
                sessionMap.put(storeNumber, session);
            }
        } catch (JSchException e) {
            throw new RuntimeException(e);
        }
        return sessionMap.get(storeNumber);
    }

    private StoreConnection getConnectionDetails() {
        if (storeConnection == null)
            try {
                storeConnection = ResourceUtils.readYamlResource("conf" + File.separator +
                        new File(System.getProperty("confPath")).getName() + File.separator +
                        "stores_connections.yaml", StoreConnection.class);
            } catch (IOException e) {
                LOGGER.error("Please check confPath: File stores_connections.yaml should be present in the specified path.");
                LOGGER.error(e.getLocalizedMessage());
            }
        return storeConnection;
    }

    public void wait(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    public ResultSet executeQuery(Connection connection, String query, String... params) {
        ResultSet resultSet;
        try {
            PreparedStatement preparedStatement = prepare(connection, query, params);
            LOGGER.info("Going to execute query: {}", getQuery(query, params));
            resultSet = preparedStatement.executeQuery();
            LOGGER.info("Query has been executed successfully");
        } catch (SQLException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
        return resultSet;
    }

    public int updateQuery(Connection connection, String query, String... params) {
        int result;
        try {
            PreparedStatement preparedStatement = prepare(connection, query, params);
            LOGGER.info("Going to execute query: {}", getQuery(query, params));
            result = preparedStatement.executeUpdate();
            LOGGER.info("Query has been executed successfully");
        } catch (SQLException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
        return result;
    }

    public PreparedStatement prepare(Connection connection, String query, String... params) {
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement(query);
            for (int i = 0; i < params.length; i++) {
                String[] param = params[i].split("::");
                switch (param[0]) {
                    case "s":
                        preparedStatement.setString(i + 1, param[1]);
                        break;
                    case "d":
                        preparedStatement.setDate(i + 1, Date.valueOf(param[1]));
                        break;
                    case "n":
                        preparedStatement.setNull(i + 1, Integer.parseInt(param[1]));
                        break;
                    case "i":
                        preparedStatement.setInt(i + 1, Integer.parseInt(param[1]));
                        break;
                    default:
                        LOGGER.error("Unknown identifier");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
        return preparedStatement;
    }

    private String getQuery(String query, String... params) {
        String result = query;
        for (String param : params) {
            String p = StringUtils.substringAfter(param, "::");
            result = result.replaceFirst("\\?", p);
        }
        return result;
    }


}
