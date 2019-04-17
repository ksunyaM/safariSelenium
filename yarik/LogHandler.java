package com.wba.rxdata.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class LogHandler {

    private static LogHandler instance = null;
    private Properties props = new Properties();
    OutputStream output = null;
    protected static final String LOG_FILE_PATH = "conf/local/default-log.properties";

    private LogHandler(){

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(LOG_FILE_PATH);

        if (inputStream != null) {
            try {
                props.load(inputStream);
                System.out.println("dsds");
                } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                throw new FileNotFoundException("property file '" + LOG_FILE_PATH + "' not found in the classpath");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public static synchronized LogHandler getInstance(){
        if (instance == null)
            instance = new LogHandler();
        return instance;
    }

    public String getValue(String propKey){
        return this.props.getProperty(propKey);
    }

}
