/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils;

import com.oneleo.test.automation.core.kafka.KafkaConstants;
import com.oneleo.test.automation.core.kafka.KafkaPropertiesReader;
import com.oneleo.test.automation.core.properties.FileMultiPropertiesLoaderImpl;
import com.oneleo.test.automation.core.properties.MultiPropertiesLoader;
import com.wba.test.utils.kafka.LogUtils;
import org.slf4j.Logger;

import java.util.Map;
import java.util.Properties;

import static com.oneleo.test.automation.core.LogUtils.log;

public class EnvironmentNameTransformUtils {
    private static final Logger LOGGER = log(LogUtils.class);

    private static final String KAFKA_TOPIC_ENVIRONMENT_NAME = "kafka.topic.environment.name";
    private static final String KAFKA_ENABLE_CONFIGURATION_FILE = "kafka.configuration.file";

    public static String clearName(String name) {
        String envTails = "(?:qe|dev|sit|devqe|perf|earlyperf|e2e)";
        String separators = "(?:_|-|~)";
        String cleanEnvName = RegExp.replaceAll(name, "^" + envTails + separators + "|" + separators + envTails + "$", "");
        return RegExp.replaceAll(cleanEnvName, "^~", "");
    }


    public static String transformName(String name) {
        if (name.startsWith("~")) return clearName(name);
        boolean getTopicFromConfigFile = false;

        try {
            getTopicFromConfigFile = Boolean.parseBoolean(KafkaPropertiesReader.read()
                    .get(KafkaConstants.DEFAULT_API_PROPERTIES_BUNDLE_KEY)
                    .getProperty(KAFKA_ENABLE_CONFIGURATION_FILE));
        } catch (Exception e) {
            LOGGER.info("Properties file default-kafka.properties is not found. Topic name can not be transformed");
            return name;
        }

        if (getTopicFromConfigFile) {
            return getEnvironmentNameFromConfig(clearName(name));
        } else {
            String environment = KafkaPropertiesReader.read()
                    .get(KafkaConstants.DEFAULT_API_PROPERTIES_BUNDLE_KEY)
                    .getProperty(KAFKA_TOPIC_ENVIRONMENT_NAME);

            return (environment != null && !environment.isEmpty()) ? environment + "-" + clearName(name) : name;
        }
    }


    public static String getEnvironmentNameFromConfig(String name) {
        Map<String, Properties> propertiesBundles = null;
        MultiPropertiesLoader multiPropertiesLoader = new FileMultiPropertiesLoaderImpl((file) -> {
            return file.getName().split("-")[0];
        });
        propertiesBundles = multiPropertiesLoader.loadAndCheck((d, fileName) -> {
            return fileName.endsWith("-configuration.properties");
        }, name + ".topic");
        return propertiesBundles.get("kafka").getProperty(name + ".topic");
    }
}


