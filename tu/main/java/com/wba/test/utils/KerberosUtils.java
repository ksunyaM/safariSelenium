/* Copyright 2018 Walgreen Co.*/
package com.wba.test.utils;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Properties;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.security.UserGroupInformation;

import com.oneleo.test.automation.core.properties.FileMultiPropertiesLoaderImpl;
import com.oneleo.test.automation.core.properties.MultiPropertiesLoader;

public class KerberosUtils {

	private static final String KERBEROS_PRINCIPAL = "kerberos.principal";
	private static final String KERBEROS_KEYTAB_PATH = "kerberos.keytab.path";
	private static Map<String, Properties> propertiesBundles = null;
	
	private static KerberosUtils instance;
	
	public static KerberosUtils authentication() {
		if (instance != null) {
			return instance;
		}

		synchronized (KerberosUtils.class) {
			if (instance != null) {
				return instance;
			}

			instance = new KerberosUtils();
			return instance;
		}
	}
	
	private KerberosUtils() {
		if(propertiesBundles == null) {
			MultiPropertiesLoader multiPropertiesLoader = new FileMultiPropertiesLoaderImpl(
					(file) -> file.getName().split("-")[0]);

			propertiesBundles = multiPropertiesLoader.loadAndCheck(
					(d, name) -> name.equals("-kerberos.properties"), KERBEROS_PRINCIPAL, KERBEROS_KEYTAB_PATH);			
		}
		
	}
	
	/**
	 * Set kerberos parameter to connect with jdbc
	 * 
	 * @param filePropertiesName prefix of kerberos properties file
	 */
	public static void init(String filePropertiesName) {
		try {
			Properties properties = propertiesBundles.get(filePropertiesName);
			String path = properties.getProperty(KERBEROS_KEYTAB_PATH);
			boolean isExists = Files.exists(Paths.get(path));
			if(!isExists) {
				new IOException("Keytab file not exists!");
			}
			Configuration conf = new org.apache.hadoop.conf.Configuration();
			conf.set("hadoop.security.authentication", "Kerberos");
			UserGroupInformation.setConfiguration(conf);
			UserGroupInformation.loginUserFromKeytab(properties.getProperty(KERBEROS_PRINCIPAL),
					path);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void init() {
		init("default");
	}
	
}
