/* Copyright 2018 Walgreen Co.*/
package com.wba.test.utils.cfs.utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.restassured.response.ValidatableResponse;


public class RestUtils {
    
    private static RestUtils instance;
    private static final Logger LOGGER = LoggerFactory.getLogger(RestUtils.class);

    private RestUtils()
    {
    }
    /**
     * method for create and get a instance of RestUtils class
     * @return
     * the related created instance
     */
    public static RestUtils getInstance()
    {
        if (instance == null)
        {
            instance = new RestUtils();
        }
        return instance; 
    }
    
    /**
     *method for read a msg from the specific REST service call response and checks the fields of the json
     *@param responseToCheck it's the response of the REST service call
     *@param keyValuePathJsonToCheckMap it's the map with a path of the specific field and the related value
     *@return true if the message is found
     */
    public Boolean checkJsonFieldFromRestService(ValidatableResponse responseToCheck, Map<String, String> keyValuePathJsonToCheckMap) {
        Map<String, String> keyValuePathJsonToCheckMODMap = new HashMap<String, String>();
        
        JSONObject jobj = new JSONObject(responseToCheck.extract().body().asString());
        List<JSONObject> listRecordJson = new ArrayList<JSONObject>();
        
        listRecordJson.add(jobj);
        
        if(listRecordJson.isEmpty()) {
            return false;
        }
        
        LOGGER.info("CHECK MAP: " + keyValuePathJsonToCheckMap);
        LOGGER.info("RESPONSE REST SERVICE CALL: " + listRecordJson);
        
        //EXTRACT THE OWN JSON OBJECT LIST FROM THE FULL JSON OBJECT LIST BY FILTER MAP
        List<JSONObject> listOwnJson = JsonUtils.getInstance().getJsonObjMessageByJsonKey(keyValuePathJsonToCheckMap, listRecordJson);
        
        if(listOwnJson == null) {
            return false;
        }
        
        //MODIFY THE FILTER MAP BY OWN JSON OBJECT LIST
        keyValuePathJsonToCheckMODMap = GenericUtils.getInstance().changeFilterMapByOwnJsonObj(keyValuePathJsonToCheckMap, listOwnJson);
        
        LOGGER.info("CHECK MAP MODIFIED: " + keyValuePathJsonToCheckMODMap);
        LOGGER.info("JSON OBJECT EXTRACT: " + listOwnJson);
        
        //CHECKS THE OWN JSON OBJECT LIST BY MY FILTER MAP
        return JsonUtils.getInstance().checkFieldJsonMessage(keyValuePathJsonToCheckMODMap, listOwnJson);
    }
}
