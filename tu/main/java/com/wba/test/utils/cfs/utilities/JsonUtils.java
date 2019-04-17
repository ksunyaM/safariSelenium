/* Copyright 2018 Walgreen Co.*/
package com.wba.test.utils.cfs.utilities;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonUtils {
    
    private static JsonUtils instance;
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtils.class);

    private JsonUtils()
    {
    }
    /**
     * method for create and get a instance of Jsonutils class
     * @return
     * the related created instance
     */
    public static JsonUtils getInstance()
    {
        if (instance == null)
        {
            instance = new JsonUtils();
        }
        return instance; 
    }
    
    /**
     *method for check a json msg with recursive implementation
     *@param keyPathJsonToCheck path of the specific field
     *@param valueToAssertCheck value expected
     *@param listRecordJsonConsumed list of the json to checks
     *@param listRecordJsonConsumedRemoved list of the json recursively reduced
     *@param keyPathJsonToCheckOrigin origin path of the specific field
     *@return true if the numMsgExpected is found
     */
    private Boolean checkFieldJsonMessageRecursive(String keyPathJsonToCheck, String valueToAssertCheck, List<JSONObject> listRecordJsonConsumed, List<JSONObject> listRecordJsonConsumedRemoved, String keyPathJsonToCheckOrigin) {
        int numbJsonObj = 0;
        Boolean flagMsgOK = false;
        String keyModJsonToCheck = null;
        String tempNewpathKeyJsonToCheck = "";
        JSONArray jsonConsumerFieldArray = null;
        JSONObject jsonConsumerField = null;
        
        LOGGER.info(keyPathJsonToCheck + ": " + valueToAssertCheck + " VALUE SEARCHED");
        LOGGER.info("LIST JSON TO VERIFY: " + listRecordJsonConsumed.toString());
        
        List<JSONObject> listJsonObjRicorsioneTemp = new ArrayList<JSONObject>();
        listJsonObjRicorsioneTemp.addAll(listRecordJsonConsumed);

        List<String> pathKeyJsonToCheck = null;

        if(!listRecordJsonConsumed.isEmpty()) {
            if(keyPathJsonToCheck.contains(".")) {
                pathKeyJsonToCheck = Arrays.asList(keyPathJsonToCheck.split("\\."));
            }else {
                pathKeyJsonToCheck = Arrays.asList(keyPathJsonToCheck);
            }
            
            for(JSONObject jsonObj: listRecordJsonConsumed) {
                for (Object key : jsonObj.keySet()) {
                    if(key.toString().equalsIgnoreCase(pathKeyJsonToCheck.get(0))) {
                        try {
                            jsonConsumerFieldArray = jsonObj.getJSONArray(pathKeyJsonToCheck.get(0));
                        } catch (JSONException ex) {
                            //NOTHING TO DO
                        }
                        try {
                            jsonConsumerField = (JSONObject) jsonObj.get(pathKeyJsonToCheck.get(0));
                        } catch (ClassCastException | JSONException ex) {
                            //NOTHING TO DO
                        }
                        break;
                    }
                }
                if (jsonConsumerFieldArray != null) {     //IF THE FIELD IS AN JSON ARRAY OR SIMPLE ARRAY
                    int x = 1;
                    for(int i = 0; i < pathKeyJsonToCheck.size(); i++) {
                        if(i >= x) {
                            tempNewpathKeyJsonToCheck += pathKeyJsonToCheck.get(i).concat(".");
                        }
                    }
                    //CHECKS IF THE RELATED JSON KEY IS ASSOCIATED A JSON ARRAY OR A SIMPLE ARRAY
                    try {
                        keyModJsonToCheck = tempNewpathKeyJsonToCheck.substring(0, (tempNewpathKeyJsonToCheck.length())-1);
                    }catch (StringIndexOutOfBoundsException ex) {
                        LOGGER.warn("THE RELATED VALUE OF THE JSON KEY " + pathKeyJsonToCheck + " IS A SIMPLE ARRAY");
                    }
                    
                    List<JSONObject> listJsonObjRicorsione = new ArrayList<JSONObject>();
                    
                    //IF THE FIELD IS AN JSON ARRAY
                    if(keyModJsonToCheck != null) {
                        //CREATE LIST JSON OBJECT FROM JSONARRAY
                        for (int iJsonArray = 0; iJsonArray < jsonConsumerFieldArray.length(); iJsonArray++) {
                            listJsonObjRicorsione.add(jsonConsumerFieldArray.getJSONObject(iJsonArray));
                        }
                        
                        listRecordJsonConsumed.remove(jsonObj);                              
                        flagMsgOK = checkFieldJsonMessageRecursive(keyModJsonToCheck, valueToAssertCheck, listJsonObjRicorsione, listRecordJsonConsumed, keyPathJsonToCheck);
                    }else {       //  IF THE FIELD IS A SIMPLE ARRAY   
                        List<String> listConditionValue = null;
                        
                        if(valueToAssertCheck.contains(",")){
                            listConditionValue = Arrays.asList(valueToAssertCheck.split(","));
                        }else {
                            listConditionValue = Arrays.asList(valueToAssertCheck);
                        }
                     
                        for(String s : listConditionValue) {
                            flagMsgOK = false;
                            for(int i = 0; i < jsonConsumerFieldArray.length(); i++) {
                                if(s.equals(jsonConsumerFieldArray.get(i).toString())) {
                                    flagMsgOK = true;
                                    LOGGER.info("THE VALUE -> " + s + " IS FOUND IN THE: " + jsonConsumerFieldArray);
                                } 
                            }
                            if(!flagMsgOK){                                    
                                LOGGER.warn("THE VALUE -> " + s + " IS NOT FOUND IN THE: " + jsonConsumerFieldArray);
                                break;
                            }
                        }
                    }
                    if((flagMsgOK)||(listRecordJsonConsumed.isEmpty())) {
                        break;
                    }
                }else if(jsonConsumerField != null) {   //IF THE FIELD IS AN JSON OBJECT
                    int x = 1;
                    for(int i = 0; i < pathKeyJsonToCheck.size(); i++) {
                        if(i >= x) {
                            tempNewpathKeyJsonToCheck += pathKeyJsonToCheck.get(i).concat(".");
                        }
                    }
                    keyModJsonToCheck = tempNewpathKeyJsonToCheck.substring(0, (tempNewpathKeyJsonToCheck.length())-1);

                    //CREATE LIST JSON OBJECT FROM JSONOBJECT
                    List<JSONObject> listJsonObjRicorsione = new ArrayList<JSONObject>();

                    listJsonObjRicorsione.add(jsonConsumerField);
                    listRecordJsonConsumed.remove(jsonObj); 
                    
                    flagMsgOK = checkFieldJsonMessageRecursive(keyModJsonToCheck, valueToAssertCheck, listJsonObjRicorsione, listRecordJsonConsumed, keyPathJsonToCheck);
                    if((flagMsgOK)||(listRecordJsonConsumed.isEmpty())) {
                        break;
                    }
                } else {     //IF THE FIELD IS A STRING
                    numbJsonObj++;
                    if(jsonObj.get(pathKeyJsonToCheck.get(0)).toString().equalsIgnoreCase(valueToAssertCheck)) {
                        assertThat(StringUtils.equalsIgnoreCase(valueToAssertCheck, jsonObj.get(pathKeyJsonToCheck.get(0)).toString()), equalTo(Boolean.TRUE));
                        flagMsgOK = true;
                        break;
                    }else {
                        if(listRecordJsonConsumed.size() == numbJsonObj) {
                            if(keyPathJsonToCheckOrigin != null) {
                                if(!listRecordJsonConsumedRemoved.isEmpty()){
                                    flagMsgOK = checkFieldJsonMessageRecursive(keyPathJsonToCheckOrigin, valueToAssertCheck, listRecordJsonConsumedRemoved, null, null);
                                    if(flagMsgOK) {
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
                if(flagMsgOK) {
                    break;
                }
            }
        }
        return flagMsgOK;
    }
    
    /**
     *method for check a json msg
     *@param keyValuePathJsonToCheckMap map with a path of the specific field and the related value
     *@param listRecordJson list of the json to checks
     *@return true if the message is found
     */
    public Boolean checkFieldJsonMessage(Map<String, String> keyValuePathJsonToCheckMap, List<JSONObject> listRecordJson) {
        Boolean flagCheckOK = false;
        List<JSONObject> listRecordJsonTempFirstTime = new ArrayList<JSONObject>();
        List<JSONObject> listRecordJsonTemp = new ArrayList<JSONObject>();
        
        listRecordJsonTempFirstTime.addAll(listRecordJson);

        //CHECK ALL FIELDS SEARCHED IN MY JSON OBJECT EXTRACT
        int i = 0;
        for (Map.Entry<String, String> entry : keyValuePathJsonToCheckMap.entrySet()){
            if(i < 1) {
                listRecordJsonTemp.addAll(listRecordJsonTempFirstTime);
            }else if (listRecordJsonTempFirstTime.isEmpty()){
                listRecordJsonTempFirstTime.addAll(listRecordJsonTemp);
            }
            String keyPathJsonToCheck = entry.getKey();
            String valueToAssertCheck = entry.getValue();

            flagCheckOK = JsonUtils.getInstance().checkFieldJsonMessageRecursive(keyPathJsonToCheck, valueToAssertCheck, listRecordJsonTempFirstTime, null, null);

            if(flagCheckOK == false) {
                LOGGER.warn(keyPathJsonToCheck + ": " + valueToAssertCheck + " NOT FOUND IN THE JSON");
                return false;
            }
            i++;
        }
        return flagCheckOK;
    }
   
    
    /**
     *method recursive to take a json object by json key
     *@param keyPathJsonToCheck path of the specific field
     *@param valueToAssertCheck value expected
     *@param listRecordJsonConsumed list of the json to checks
     *@param listRecordJsonConsumedRemoved list of the json recursively reduced
     *@param keyPathJsonToCheckOrigin origin path of the specific field
     *@param ownReferenceJsonObj json object to extract
     *@return JSON Object extracted related to my filter
     */
    private JSONObject getJsonObjMessageByJsonKeyRecursive(String keyPathJsonToCheck, String valueToAssertCheck, List<JSONObject> listRecordJsonConsumed, List<JSONObject> listRecordJsonConsumedRemoved, String keyPathJsonToCheckOrigin, JSONObject ownReferenceJsonObj) {
        int numbJsonObj = 0;
        JSONObject ownJsonObjFound = null;
        String keyModJsonToCheck;
        String tempNewpathKeyJsonToCheck = "";
        JSONArray jsonConsumerFieldArray = null;
        JSONObject jsonConsumerField = null;
        
        LOGGER.info(keyPathJsonToCheck + ": " + valueToAssertCheck + " VALUE SEARCHED TO EXTRACT OWN JSON OBJ");
        LOGGER.info("LIST JSON TO SEARCH MY JSON OBJ: " + listRecordJsonConsumed.toString());
        
        List<JSONObject> listJsonObjRicorsioneTemp = new ArrayList<JSONObject>();
        listJsonObjRicorsioneTemp.addAll(listRecordJsonConsumed);

        List<String> pathKeyJsonToCheck = null;

        if(!listRecordJsonConsumed.isEmpty()) {
            if(keyPathJsonToCheck.contains(".")) {
                pathKeyJsonToCheck = Arrays.asList(keyPathJsonToCheck.split("\\."));
            }else {
                pathKeyJsonToCheck = Arrays.asList(keyPathJsonToCheck);
            }
            
            for(JSONObject jsonObj: listRecordJsonConsumed) {
                for (Object key : jsonObj.keySet()) {
                    if(key.toString().equalsIgnoreCase(pathKeyJsonToCheck.get(0))) {
                        try {
                            jsonConsumerFieldArray = jsonObj.getJSONArray(pathKeyJsonToCheck.get(0));
                        } catch (JSONException ex) {
                            //NOTHING TO DO
                        }
                        try {
                            jsonConsumerField = (JSONObject) jsonObj.get(pathKeyJsonToCheck.get(0));
                        } catch (ClassCastException | JSONException ex) {
                            //NOTHING TO DO
                        }
                        break;
                    }
                }
                if (jsonConsumerFieldArray != null) {            //IF THE FIELD IS AN JSON ARRAY
                    int x = 1;
                    for(int i = 0; i < pathKeyJsonToCheck.size(); i++) {
                        if(i >= x) {
                            tempNewpathKeyJsonToCheck += pathKeyJsonToCheck.get(i).concat(".");
                        }
                    }
                    keyModJsonToCheck = tempNewpathKeyJsonToCheck.substring(0, (tempNewpathKeyJsonToCheck.length())-1);

                    //CREATE LIST JSON OBJECT FROM JSONARRAY
                    List<JSONObject> listJsonObjRicorsione = new ArrayList<JSONObject>();
                    for (int iJsonArray = 0; iJsonArray < jsonConsumerFieldArray.length(); iJsonArray++) {
                        listJsonObjRicorsione.add(jsonConsumerFieldArray.getJSONObject(iJsonArray));
                    }
                    listRecordJsonConsumed.remove(jsonObj);
                    
                    ownJsonObjFound = getJsonObjMessageByJsonKeyRecursive(keyModJsonToCheck, valueToAssertCheck, listJsonObjRicorsione, listRecordJsonConsumed, keyPathJsonToCheck, jsonObj);
                    if((ownJsonObjFound != null)||(listRecordJsonConsumed.isEmpty())) {
                        return ownJsonObjFound;
                    }
                }else if(jsonConsumerField != null) {        //IF THE FIELD IS AN JSON OBJECT
                    int x = 1;
                    for(int i = 0; i < pathKeyJsonToCheck.size(); i++) {
                        if(i >= x) {
                            tempNewpathKeyJsonToCheck += pathKeyJsonToCheck.get(i).concat(".");
                        }
                    }
                    keyModJsonToCheck = tempNewpathKeyJsonToCheck.substring(0, (tempNewpathKeyJsonToCheck.length())-1);

                    //CREATE LIST JSON OBJECT FROM JSONOBJECT
                    List<JSONObject> listJsonObjRicorsione = new ArrayList<JSONObject>();

                    listJsonObjRicorsione.add(jsonConsumerField);

                    listRecordJsonConsumed.remove(jsonObj); 
                    ownJsonObjFound = getJsonObjMessageByJsonKeyRecursive(keyModJsonToCheck, valueToAssertCheck, listJsonObjRicorsione, listRecordJsonConsumed, keyPathJsonToCheck, jsonObj);
                    if((ownJsonObjFound != null)||(listRecordJsonConsumed.isEmpty())) {
                        return ownJsonObjFound;
                    }
                } else {                                      //IF THE FIELD IS A STRING
                    numbJsonObj++;
                    if(jsonObj.get(pathKeyJsonToCheck.get(0)).toString().equalsIgnoreCase(valueToAssertCheck)) {
                        assertThat(StringUtils.equalsIgnoreCase(valueToAssertCheck, jsonObj.get(pathKeyJsonToCheck.get(0)).toString()), equalTo(Boolean.TRUE));
                        return jsonObj;
                    }else {
                        if(listRecordJsonConsumed.size() == numbJsonObj) {
                            if(keyPathJsonToCheckOrigin != null) {
                                if(!listRecordJsonConsumedRemoved.isEmpty()){
                                    ownJsonObjFound = getJsonObjMessageByJsonKeyRecursive(keyPathJsonToCheckOrigin, valueToAssertCheck, listRecordJsonConsumedRemoved, null, null, null);
                                    if(ownJsonObjFound != null) {
                                        return ownJsonObjFound;
                                    }else {
                                        return null;
                                    }
                                }else {
                                    ownReferenceJsonObj = null;
                                }
                            }
                        }
                    }
                }
            }
        }
        if((ownReferenceJsonObj != null) && (ownJsonObjFound == null)) {
            return ownReferenceJsonObj;
        }else if(ownJsonObjFound != null){
            return ownJsonObjFound;
        }else {
            return null;
        }
    }
    
    /**
     *method to take a list a json object by json key
     *@param keyValuePathJsonToCheckMap map with a path of the specific field and the related value
     *@param listRecordJson list of the json to checks
     *@return the list json object searched by json key
     */
    public List<JSONObject> getJsonObjMessageByJsonKey(Map<String, String> keyValuePathJsonToCheckMap, List<JSONObject> listRecordJson) {
        Boolean flagExistJsonObj = false;
        JSONObject jsonObjSearched= null;
        List<JSONObject> listRecordJsonTempFirstTime = new ArrayList<JSONObject>();

        //EXTRACT MY JSON OBJECT
        for (Map.Entry<String, String> entry : keyValuePathJsonToCheckMap.entrySet()){
            String keyPathJsonToCheck = entry.getKey();
            String valueToAssertCheck = entry.getValue();

            jsonObjSearched = JsonUtils.getInstance().getJsonObjMessageByJsonKeyRecursive(keyPathJsonToCheck, valueToAssertCheck, listRecordJson, null, null, null);
            break;
        }

        if(jsonObjSearched != null) {
            flagExistJsonObj = true;
        }else {
            LOGGER.warn("THE JSON OBJ SEARCHED DOES NOT EXISTS -> " + jsonObjSearched);
            return null;
        }
        LOGGER.info("THE JSON OBJ SEARCHED IS: " + jsonObjSearched);
        assertThat(flagExistJsonObj, equalTo(Boolean.TRUE));

        listRecordJsonTempFirstTime.add(jsonObjSearched);
        
        return listRecordJsonTempFirstTime;
    }
}
