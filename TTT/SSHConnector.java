package common;


import com.jcraft.jsch.*;
import com.oneleo.test.automation.core.LogUtils;
import com.oneleo.test.automation.core.properties.FileMultiPropertiesLoaderImpl;
import com.oneleo.test.automation.core.properties.MultiPropertiesLoader;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.slf4j.Logger;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

import static java.nio.charset.StandardCharsets.UTF_8;

public class SSHConnector {

    private static final Logger LOGGER = LogUtils.log(common.SSHConnector.class.getName());
    private int port;
    private Session session;
    private int timeout;
    private String template;

    public SSHConnector() {
        this.template = "default";
        port = 22;
        timeout = 60000;
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

    public void connect() {
        try {
            JSch jsch = new JSch();
            session = jsch.getSession(getProperty("ssh.user"), getProperty("ssh.host"), port);
            session.setPassword(getProperty("ssh.password"));
            session.setConfig("StrictHostKeyChecking", "no");
            session.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
            session.connect(timeout);
        } catch (JSchException e) {
            LOGGER.error(e.toString(), e);
        }
    }


    public String executeCommand(String command) {
        //   connect();
        String result = "";


        try {
            Channel channel = session.openChannel("exec");
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            ((ChannelExec) channel).setOutputStream(outputStream);
            ((ChannelExec) channel).setCommand(command);
            ((ChannelExec) channel).setErrStream(System.err);

            channel.connect();
            while (channel.getExitStatus() == -1) {
                Thread.sleep(2000);
            }

            result = new String(outputStream.toByteArray(), UTF_8);
            channel.disconnect();

        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
        return result;
    }

    public String executeCommandShell(ArrayList commands) {
        //connect();

        String result = "";

        StringBuilder sb = new StringBuilder();

        commands.forEach(s -> sb.append(s+" \n"));



        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            ByteArrayInputStream input = new ByteArrayInputStream(sb.toString().getBytes(UTF_8));

            Channel channel = session.openChannel("shell");
            ((ChannelShell) channel).setOutputStream(output);
            ((ChannelShell) channel).setInputStream(input);


            channel.connect();
            while (channel.getExitStatus() == -1) {
                Thread.sleep(1000);
            }
            result = new String(output.toByteArray(), UTF_8);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}