package com.wba.dataanalytics.api.test.common;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.oneleo.test.automation.core.LogUtils;
import com.oneleo.test.automation.core.properties.FileMultiPropertiesLoaderImpl;
import com.oneleo.test.automation.core.properties.MultiPropertiesLoader;
import com.wba.test.utils.DataStorage;
import org.slf4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static junit.framework.TestCase.fail;

public class SSHConnector {

    private static final Logger LOGGER = LogUtils.log(SSHConnector.class.getName());
    private static ChannelShell channel;
    private int port;
    private Session session;
    private int timeout;
    private String template;

    public SSHConnector(String template) {
        this.template = template;
        port = 22;
        timeout = 60000;
    }

    public SSHConnector() {
        this.template = "default";
        port = 22;
        timeout = 60000;
    }

    private static void readChannelOutput(Channel channel) {

        byte[] buffer = new byte[3072];

        try {
            InputStream in = channel.getInputStream();
            String line = "";
            while (true) {
                while (in.available() > 0) {
                    int i = in.read(buffer, 0, 3072);
                    if (i < 0) {
                        break;
                    }
                    line = new String(buffer, 0, i);
                    System.out.println(line);
                }

                if (line.contains("logout")) {
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

    public String getProperty(String propertyName) {
        MultiPropertiesLoader multiPropertiesLoader = new FileMultiPropertiesLoaderImpl((file) -> {
            return file.getName().split("-")[0];
        });
        Map<String, Properties> propertiesBundles = multiPropertiesLoader.loadAndCheck((d, fileName) -> {
            return fileName.endsWith("-ssh.properties");
        }, "ssh.host");
        return propertiesBundles.get(template).getProperty(propertyName);
    }

    private String getUserCredentialsProperty(String propertyName) {
        Properties userCredentials = new Properties();
        File currentDir = new File("");
        try {
            userCredentials.load(new FileInputStream(currentDir.getAbsolutePath()+ "/" + System.getProperty("confPath") + "/" + "default-ssh.properties"));
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.warn("server_connection.properties file not found in {}", System.getProperty("user.home"));
            throw new RuntimeException();
        }
        return userCredentials.getProperty(propertyName);
    }

    private String getHostAndEnvProperty(String propertyName) {
        Properties hostAndEnvProperties = new Properties();
        try {
            hostAndEnvProperties.load(new FileInputStream(System.getProperty("user.dir") + File.separator + "src\\test\\resources\\conf\\devqe\\" + "default-etl.properties"));
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.warn("default-etl.properties file not found in {}", System.getProperty("user.dir") + File.separator + "api-test\\src\\test\\resources\\conf\\devqe\\");
            throw new RuntimeException();
        }
        return hostAndEnvProperties.getProperty(propertyName);
    }

    public void close() {
        session.disconnect();
        channel.disconnect();
    }

    public Session connectToAmbari() {

        JSch jSch = new JSch();
        try {
            String username = getUserCredentialsProperty("ssh.username");
            String password = getUserCredentialsProperty("ssh.password");
            session = jSch.getSession(username, getProperty("ssh.host"), port);
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.setPassword(password);
            System.out.println("Connecting SSH to " + getProperty("ssh.host") + " - Please wait for few seconds... ");
            session.connect();
            System.out.println("Connected!");
        } catch (Exception e) {
            System.out.println("An error occurred while connecting to " + getProperty("ssh.host") + ": " + e);
        }

        return session;
    }

    public void executeCommandsWithoutLog(List<String> commands) {
        try {
            Channel channel = getChannel();

            System.out.println("Sending commands...");
            sendCommands(channel, commands);

//            readChannelOutput(channel);
            System.out.println("Finished sending commands!");

        } catch (Exception e) {
            System.out.println("An error ocurred during executeCommands: " + e);
        }
    }

    public void executeCommands(List<String> commands) {
        try {
            Channel channel = getChannel();

            System.out.println("Sending commands...");
            sendCommands(channel, commands);

            readChannelOutput(channel);
            System.out.println("Finished sending commands!");

        } catch (Exception e) {
            System.out.println("An error ocurred during executeCommands: " + e);
        }
    }

    public List<String> executeCommandsYarn(List<String> commands) {
        try {
            Channel channel = getChannel();

            System.out.println("Sending commands...");
            sendCommands(channel, commands);

            System.out.println("Finished sending commands!");
            return readChannelOutputYarn(channel);

        } catch (Exception e) {
            System.out.println("An error ocurred during executeCommands: " + e);
            return null;
        }
    }


    private List<String> readChannelOutputYarn(Channel channel) {
        List<String> result = new ArrayList<>();
        byte[] buffer = new byte[3072];

        try {
            InputStream in = channel.getInputStream();
            String line = "";
            while (true) {
                while (in.available() > 0) {
                    int i = in.read(buffer, 0, 3072);
                    if (i < 0) {
                        break;
                    }
                    line = new String(buffer, 0, i);
                    result.add(line);
                    System.out.println(line);
                }

                if (line.contains("logout")) {
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
        return result;
    }

    private Channel getChannel() {
        if (channel == null || !channel.isConnected()) {
            try {
                channel = (ChannelShell) getSession().openChannel("shell");
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

    private Session getSession() {
        if (session == null || !session.isConnected()) {
            session = connectToAmbari();
        }
        return session;
    }

    /**
     * Copy the files from Remote to Local
     *
     * @param remoteFilePath
     * @param localFilePathLocation
     * @param fileName
     */
    public void copyLocalFileToRemote(String remoteFilePath, String localFilePathLocation, String fileName) {

        String username = getUserCredentialsProperty("ssh.username");
        String password = getUserCredentialsProperty("ssh.password");
        String hostName = getHostAndEnvProperty("host");
        String tempLocation = getHostAndEnvProperty("temppath");
        String sshSudo = getHostAndEnvProperty("ssh.sudo");

        List<String> delTempDirFiles = new ArrayList<>();
        delTempDirFiles.add("cd " + tempLocation);
        delTempDirFiles.add("rm *");
        connectToAmbari();
        LOGGER.info("Commands - " + delTempDirFiles.toString());
        executeCommands(delTempDirFiles);
        delTempDirFiles.clear();

        String command = "pscp -pw " + password + " " + localFilePathLocation + " " +
                username + "@" + hostName +
                ":" + tempLocation;

        try {
            LOGGER.info("Copying {}" + localFilePathLocation + " to {}" + remoteFilePath);
            Runtime.getRuntime().exec(command).waitFor();
            LOGGER.info("File copied to remote - {}" + remoteFilePath);
        } catch (Exception e) {
            fail("Error occurred while executing command: " + command);
        }

        LOGGER.info("File copied !!!");

        List<String> jobCommands = new ArrayList<>();
        jobCommands.add("sudo su - " + sshSudo);
        jobCommands.add("kinit -kt .keytabs/" + sshSudo + ".keytab " + sshSudo);
        jobCommands.add("rm " + remoteFilePath + fileName);
        jobCommands.add("cp " + tempLocation + fileName + " " + remoteFilePath);
        //connectToAmbari();
        LOGGER.info("Commands - " + jobCommands.toString());
        executeCommands(jobCommands);
        jobCommands.clear();

    }

    public void copyRemoteFileToLocal(String remoteFilePath, String localFilePath, String fileType) {

        String username = getUserCredentialsProperty("ssh.username");
        String password = getUserCredentialsProperty("ssh.password");
        String hostName = getHostAndEnvProperty("host");

        String command = "pscp -pw " + password + " " +
                username + "@" + hostName +
                ":" + remoteFilePath + fileType + " " + localFilePath;
        try {
            LOGGER.info("Copying {}" + remoteFilePath + " to {}" + localFilePath);
            Runtime.getRuntime().exec(command).waitFor();
            LOGGER.info("File copied to local - {}" + localFilePath);
        } catch (Exception e) {
            fail("Error occurred while executing command: " + command);
        }

        LOGGER.info("File copied !!!");
    }

    public void userExecutesPagForecastJob(String joblocation, String jobname) {

        String sshSudo = getHostAndEnvProperty("ssh.sudo");
        List<String> jobCommands = new ArrayList<>();
        jobCommands.add("sudo su - " + sshSudo);
        jobCommands.add("kinit -kt .keytabs/" + sshSudo + ".keytab " + sshSudo);
        jobCommands.add("cd " + joblocation);
        jobCommands.add("sh " + jobname);
        connectToAmbari();
        LOGGER.info("Commands - " + jobCommands.toString());
        executeCommandsWithoutLog(jobCommands);
        jobCommands.clear();
    }

    public void userExecutesForecastJob(String joblocation, String jobname) {

        String sshSudo = getHostAndEnvProperty("ssh.sudo");
        List<String> jobCommands = new ArrayList<>();
        jobCommands.add("sudo su - " + sshSudo);
        jobCommands.add("kinit -kt .keytabs/" + sshSudo + ".keytab " + sshSudo);
        jobCommands.add("cd " + joblocation);
        jobCommands.add("sh " + jobname);
        connectToAmbari();
        LOGGER.info("Commands - " + jobCommands.toString());
        executeCommands(jobCommands);
        jobCommands.clear();
    }

}