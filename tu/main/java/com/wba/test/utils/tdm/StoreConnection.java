/*
 * Copyright 2018 Walgreen Co.
 */
package com.wba.test.utils.tdm;

import java.util.HashMap;
import java.util.Map;

public class StoreConnection {
    private String hostStoreDB;
    private String hostStoreSSH;
    private String hostCentralDB;
    private Map<String, String> centralDB = new HashMap<>();
    private Map<String, Map<String, String>> storeSSH = new HashMap<>();
    private Map<String, Map<String, String>> storeDB = new HashMap<>();


    public Map<String, String> getCentralDB(){
        return centralDB;
    }
    public Map<String, Map<String, String>> getStoreSSH(){return storeSSH;}
    public Map<String, Map<String, String>> getStoreDB(){return storeDB;}
    public String getHostStoreSSH(){return hostStoreSSH;}
    public String getHostStoreDB(){return hostStoreDB;}
    public String getHostCentralDB(){return hostCentralDB;}

}