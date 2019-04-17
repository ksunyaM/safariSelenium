/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.StringReader;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static com.oneleo.test.automation.core.LogUtils.log;

public class PropertiesReader {
    private static final Logger LOGGER = log(PropertiesReader.class);

    public static Properties read(String resource, boolean isConf) {
        Properties properties = new Properties();

        String _r = resource + (resource.endsWith(".properties") ? "" : ".properties");
        String path = isConf ? getResourcePathFromConfPath() + _r : _r;
        try {
            properties.load(new StringReader(ResourceUtils.getResourceAsString(path)));
        } catch (Exception ex) {
            LOGGER.error("Properties file not found: " + ex.toString());
            throw new RuntimeException(ex);
        }
        return properties;
    }

    public static Properties readFromAbsolutePath(String path) {
        Properties props = new Properties();
        try {
            props.load(new FileInputStream(path));
            return props;
        } catch (Exception e) {
            LOGGER.error("Properties file not found: " + path);
            throw new RuntimeException(e);
        }
    }

    public static Properties read(String resource) {
        return read(resource, false);
    }

    private static String getResourcePathFromConfPath() {
        String envName = new File(System.getProperty("confPath")).getName();
        return "conf" + File.separator + envName + File.separator;
    }

    public static void updateProperties(String filePath, Map<String, String> data) throws ConfigurationException {
        PropertiesConfiguration config = new PropertiesConfiguration(new File(filePath));

        data.keySet().forEach(key -> config.setProperty(key, data.get(key)));

        config.save();
    }

    public static void clearProperties(String filePath, List<String> data) throws ConfigurationException {
        PropertiesConfiguration config = new PropertiesConfiguration(new File(filePath));

        data.forEach(config::clearProperty);

        config.save();
    }
}

