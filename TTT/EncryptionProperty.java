package com.wba.rxrintegration.api.test.Pi_eRxSendMessage;

import com.voltage.securedata.enterprise.VeException;
import com.wba.test.utils.PropertiesReader;

import java.util.Properties;

public class EncryptionProperty {

    private final String PROPERTY_POLICY_URL = "encrypt.policyxurl";
    private final String PROPERTY_SHARED_SECRET = "encrypt.sharedSecret";
    private final String PROPERTY_TRUST_STORE = "truststore";
    private final String PROPERTY_CACHE_PATH = "cachepath";
    private String propertiesFileName;
    private Properties properties = new Properties();
    String policyURL = null;
    String sharedSecret = null;
    String trustStorePath = null;
    String cachePath = null;
    private void readProperyFile() {
        properties = PropertiesReader.read(propertiesFileName, true);
        policyURL = properties.getProperty(PROPERTY_POLICY_URL);
        sharedSecret = properties.getProperty(PROPERTY_SHARED_SECRET);
        trustStorePath = properties.getProperty(PROPERTY_TRUST_STORE);
        cachePath = properties.getProperty(PROPERTY_CACHE_PATH);
    }

    public EncryptionProperty() throws VeException {
        this("default");

    }
    public EncryptionProperty(String propertiesPrefix) throws VeException {
        propertiesFileName = propertiesPrefix + "-encryption.properties";
        readProperyFile();

    }
}
