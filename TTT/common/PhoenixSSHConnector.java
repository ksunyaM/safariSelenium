package com.wba.dataanalytics.api.test.common;


import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.oneleo.test.automation.core.LogUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import java.io.*;
import java.util.*;

public class PhoenixSSHConnector extends SSHConnector{

    private static final Logger LOGGER = LogUtils.log(PhoenixSSHConnector.class.getName());
    private static String output;
    private int port;
    private Session session;
    private static ChannelShell channel;
    private int timeout;
    private String template;


    public PhoenixSSHConnector() {
        this.template = "default";
        port = 22;
        timeout = 60000;
    }

    public String getUserCredentialsForPhoenixDBConn(String propertyName) {
        Properties userCredentials = new Properties();
        File currentDir = new File("");
        try {
            userCredentials.load(new FileInputStream(currentDir.getAbsolutePath()+ "/" + System.getProperty("confPath") + "/" + "phoenixdbconn.properties"));
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.warn("phoenixdbconn.properties file not found in {}", System.getProperty("confPath"));
            throw new RuntimeException();
        }
        return userCredentials.getProperty(propertyName);
    }

    public void close() {
        session.disconnect();
        channel.disconnect();
    }

    public void executeCommandsForPhoenixDB(List<String> commands) {
        try {
            Channel channel = getChannelForPhoenixDB();

            System.out.println("Sending commands...");
            sendCommands(channel, commands);

            readChannelOutput(channel);
            System.out.println("Finished sending commands!");

        } catch (Exception e) {
            System.out.println("An error ocurred during executeCommands: " + e);
        }
    }

    private Channel getChannelForPhoenixDB() {
        if (channel == null || !channel.isConnected()) {
            try {
                channel = (ChannelShell) getSessionForPhoenixDB().openChannel("shell");
                channel.connect();

            } catch (Exception e) {
                System.out.println("Error while opening channel: " + e);
            }
        }
        return channel;
    }

    private void sendCommands(Channel channel, List<String> commands) {
        try {
            PrintStream out = new PrintStream(channel.getOutputStream());

            out.println("#!/bin/bash");
            for (String command : commands) {
                LOGGER.info("Command execution server result: {} ", command);
                out.println(command);
            }
            out.println("exit");

            out.flush();
        } catch (Exception e) {
            System.out.println("Error while sending commands: " + e);
        }
    }

    private static void readChannelOutput(Channel channel) {

        byte[] buffer = new byte[307200000];

        try {
            InputStream in = channel.getInputStream();
            String line = "";
            while (true) {
                while (in.available() > 0) {
                    int i = in.read(buffer, 0, 307200000);
                    if (i < 0) {
                        break;
                    }
                    line = new String(buffer, 0, i);
                    output = line;
                    System.out.println(line);
                }
                Thread.sleep(10000);
                if (line.contains("logout") || line.contains("exit")) {
                    break;
                }

                if (channel.isClosed()) {
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (Exception ee) {
                }
            }
        } catch (Exception e) {
            System.out.println("Error while reading channel output: " + e);
        }
    }

    private Session getSessionForPhoenixDB() {
        if (session == null || !session.isConnected()) {
            session = connectToPhoenixDB();
        }
        return session;
    }

    public Session connectToPhoenixDB() {

        JSch jSch = new JSch();
        try {
            String username = getUserCredentialsForPhoenixDBConn("ssh.username");
            String password = getUserCredentialsForPhoenixDBConn("ssh.password");
            session = jSch.getSession(username, getUserCredentialsForPhoenixDBConn("ssh.host"), port);
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.setPassword(password);
            System.out.println("Connecting SSH to " + getUserCredentialsForPhoenixDBConn("ssh.host") + " - Please wait for few seconds... ");
            session.connect();
            System.out.println("Connected!");
        } catch (Exception e) {
            System.out.println("An error occurred while connecting to " + getUserCredentialsForPhoenixDBConn("ssh.host") + ": " + e);
        }

        return session;
    }

    public static List<Map<String, String>> extractDataIntoCorrectFormat() {
        String output = PhoenixSSHConnector.output;

        String rowsSelected = "";
        if (output.contains("rows selected")) {
            rowsSelected = StringUtils.substringBeforeLast(output, "rows selected");
            output = rowsSelected;
        }

        String setMaxwidth = "";
        if (output.contains("!set maxwidth 200")) {
            setMaxwidth = StringUtils.substringAfterLast(output, "!set maxwidth 200");
            output = setMaxwidth;
        }

        String afterPipe = "";
        if (output.contains("|")) {
            afterPipe = StringUtils.substringAfter(output, "|");
            output = afterPipe;
        }

        String beforeLastPipe = "";
        if (output.contains("|")) {
            beforeLastPipe = StringUtils.substringBeforeLast(output, "|");
            output = beforeLastPipe;
        }

        String beforeDash = "";
        String columnNames = "";
        if (output.contains("-")) {
            beforeDash = StringUtils.substringBefore(output, "--");
            columnNames = beforeDash;
        } else {
            columnNames = beforeLastPipe;
        }

        String afterDash = "";
        String columnValues = "";
        if (output.contains("-")) {
            afterDash = StringUtils.substringAfterLast(output, "--");
            columnValues = afterDash;
        }

        String removeCharFromColumnName = columnNames.replaceAll("\\[1;32m", "");
        String removeCharFromColumnValues = columnValues.replaceAll("\\[1;32m", "");

        String removeBracketsColNames = removeCharFromColumnName.replaceAll("\\[", "");
        String removeBracketsColValues = removeCharFromColumnValues.replaceAll("\\[", "");

        String removeSpacesColNames = removeBracketsColNames.replaceAll(" ", "");
        String removeSpacesColValues = removeBracketsColValues.replaceAll(" ", "");

        String[] splitColumnNames = removeSpacesColNames.split("\\|");
        String[] splitColumnValues = removeSpacesColValues.split("\\|");
        List<Map<String, String>> list = new ArrayList<>();
        try {
        List<String> removeColNameFirstM = new ArrayList<>();
        for (String string1 : splitColumnNames) {
            if (string1.substring(1, 2).equals("m")) {
                removeColNameFirstM.add(string1.substring(2));
            } else {
                removeColNameFirstM.add(string1);
            }
        }

        List<String> listColNameFirstM = removeColNameFirstM;

        List<String> removeColNameFirst1 = new ArrayList<>();
        for (String string2 : listColNameFirstM) {
            if (string2.substring(1, 2).equals("1")) {
                removeColNameFirst1.add(string2.substring(2));
            } else {
                removeColNameFirst1.add(string2);
            }
        }
        List<String> listColNameFirst1 = removeColNameFirst1;

        List<String> removeColNameSecondM = new ArrayList<>();
        for (String string3 : listColNameFirst1) {
            if (string3.substring(0, 1).equals("m")) {
                removeColNameSecondM.add(string3.substring(1));
            } else {
                removeColNameSecondM.add(string3);
            }
        }
        List<String> listColNameSecondM = removeColNameSecondM;

        List<String> removeColNameLastM = new ArrayList<>();
        for (String string4 : listColNameSecondM) {
            String substring = string4.substring(string4.length() - 2, string4.length() - 1);
            if (substring.equals("m")) {
                removeColNameLastM.add(string4.substring(0, string4.length() - 2));
            } else {
                removeColNameLastM.add(string4);
            }
        }

        List<String> listColNameLastM = removeColNameFirstM;

        List<String> removeColValueFirstM = new ArrayList<>();
        for (String string5 : splitColumnValues) {
            if (!string5.isEmpty()) {
                if (string5.substring(1, 2).equals("m")) {
                    removeColValueFirstM.add(string5.substring(2));
                } else {
                    removeColValueFirstM.add(string5);
                }
            }
        }

        List<String> listColumnNamesFinal = removeColNameLastM;
        List<String> listColumnValuesFinal = removeColValueFirstM;


            HashMap<String, String> map = new HashMap<>();
            int i = listColumnNamesFinal.size() - 1;
            for (String colValues : listColumnValuesFinal) {
                if (!colValues.contains("\n")) {
                    map.put(listColumnNamesFinal.get(i), colValues);
                    i++;
                } else {
                    i = 0;
                    list.add(map);
                    map = new HashMap<>();
                    continue;
                }
            }
            list.add(map);

            if (list.size() != 1) {
                list.remove(0);
            }
        } catch (StringIndexOutOfBoundsException e) {
            e.printStackTrace();
            LOGGER.error("Table Column Name Might Not Be Present!!!");
        }

        return list;
    }


}
