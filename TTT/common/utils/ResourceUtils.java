package com.wba.dataanalytics.api.test.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ResourceUtils {

	private static final String DEFAULT_DIR_PATH_PROPERTY = "confPath";

	public static Map<String, String> getPropertiesFileAsMap(String fileName) {
		String confPath = System.getProperty(DEFAULT_DIR_PATH_PROPERTY);
		String propertiesDirectoryPath = StringUtils.EMPTY;
		if (fileName.equalsIgnoreCase("default-analytics.properties")) {
			propertiesDirectoryPath = System.getProperty("user.dir") + File.separator + confPath;
		} else {
			if (StringUtils.isBlank(confPath)) {
				throw new IllegalArgumentException(
						"Path system properties is not set. Please use -D" + DEFAULT_DIR_PATH_PROPERTY);
			} else {

				propertiesDirectoryPath = System.getProperty("user.dir") + File.separator + confPath;
			}
		}
		Properties properties = new Properties();
		File kafkaPropertiesFile = new File(propertiesDirectoryPath + File.separator + fileName);
		if (!kafkaPropertiesFile.exists()) {
			if (fileName.equalsIgnoreCase("default-analytics.properties")) {
				throw new IllegalArgumentException(
						"no Data analytics configuration properties found in the path '" + propertiesDirectoryPath);
			}
			throw new IllegalArgumentException(
					"no kafka configuration properties found in the path '" + propertiesDirectoryPath);
		}
		try {
			properties.load(new FileInputStream(kafkaPropertiesFile));
		} catch (Exception e) {
			// as we checked early if any file exists. This code branch
			// should never be reached
			throw new IllegalArgumentException("no properties file found for '" + kafkaPropertiesFile.getName() + "'");
		}

		Map<String, String> mapOfProperties = new HashMap(properties);

		return mapOfProperties;
	}
}
