
/* Copyright 2018 Walgreen Co.*/
package com.wba.test.utils.cfs.utilities;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class GenericUtils {

	private static GenericUtils instance;
	private Map<String, String> randMap = new HashMap<String, String>();
	private String fieldValue;
    private static final Logger LOGGER = LoggerFactory.getLogger(GenericUtils.class);


	private GenericUtils() {
	}

	/**
	 * method for create and get a instance of RestUtils class
	 * 
	 * @return the related created instance
	 */
	public static GenericUtils getInstance() {
		if (instance == null) {
			instance = new GenericUtils();
		}
		return instance;
	}

    /**
     * method for create a random string
     * @param length the length of the string that i want generate
     * @return the random string
     */
	public String generateRandomString(int length) {
		String valueRandom = StringUtils.EMPTY;
		Random r = new Random();
		int Low = 1;
		int High = 0;
		for (int i = length; i > -1; i--) {
			High = (int) (High + (9 * (Math.pow(10, (i - 1)))));
		}
		valueRandom = Integer.toString(r.nextInt(High - Low) + Low);
		return valueRandom;
	}

	/**
	 * method for adds a value as a concatenation of the input value with a
	 * random value to the hashmap
	 * 
	 * @param the
	 *            key of the random field
	 * @param the
	 *            value in input that you want to concatenate with the random
	 *            value
	 * @param length
	 *            the length of the string that i want generate
	 * @return a randomic string
	 */
	public String setFieldValue(String keyField, String valueField, int length) {

		this.randMap.put(keyField, valueField + RandomStringUtils.randomAlphanumeric(length));
		return valueField + RandomStringUtils.randomAlphanumeric(length);
	}

	/**
	 * Method that returns a new value to add as a concatenation of the input
	 * value with a random value 
	 * 
	 * 
	 * @return a new value to add as a concatenation of the input value with a
	 *         random value
	 */
	public String getFieldValue(String keyField) {
		return randMap.get(keyField);
	}

	/**
	 * Method that returns a new value to add as a concatenation of the input
	 * value with a random value
	 * 
	 * 
	 * @return a new value to add as a concatenation of the input value with a
	 *         random value
	 */

	public String getFieldValue() {
		return fieldValue;
	}

	/**
	 * Method that creates a new value to add as a concatenation of the input
	 * value with a random value
	 * 
	 * 
	 * @param the
	 *            value in input that you want to concatenate with the random
	 *            value
	 * @param length
	 *            the length of the string that i want generate
	 * @return a new value to add as a concatenation of the input value with a
	 *         random value
	 */
	public String setFieldValue(String fieldValue, int length) {
		return this.fieldValue = fieldValue + RandomStringUtils.randomAlphanumeric(length);
	}
	
    /**
     * method for search the last index of the target found in the string.
     * @param source String to checks
     * @param target character to search
     * @return the index of the last occurence about target in the related source
     */
	public int countCharOccurrences(String source, char target) {
	    int index = 0;
	    String sourceTemp = null;
	    for (int i = 0; i < source.length(); i++) {
	        if(i<1) {
	            sourceTemp = source;
	        }
	        if (source.charAt(i) == target) {
	            index += sourceTemp.indexOf(".");
	            sourceTemp = sourceTemp.substring(sourceTemp.indexOf(".")+1, sourceTemp.length());
	        }
	    }
	    return index;
	}
	
    /**
     * method for change the filter map keyValuePathJsonToCheckMap by listOwnJson
     * @param keyValuePathJsonToCheckMap map with a path of the specific field and the related value
     * @param listOwnJson list of the json to checks
     * @return the filter map changed by listOwnJson
     */
    public Map<String, String> changeFilterMapByOwnJsonObj(Map<String, String> keyValuePathJsonToCheckMap, List<JSONObject> listOwnJson) {
        int i = 0;
        int numbOfDotinOwnKey = 0;
        String originalRootKey = null;
        
        String keyPathJsonToCheck = null;
        String valuePathJsonToCheck = null;
        Map<String, String> keyValuePathJsonToCheckMODMap = new HashMap<String, String>();
        Map<String, String> keyValuePathJsonToCheckMODTempMap = new HashMap<String, String>();

        for (Map.Entry<String, String> entry : keyValuePathJsonToCheckMap.entrySet()){
            if(i < 1) {
                //CALCULATE THE NUMBER OF OCCURENCY OF THE DOT IN THE FIRST JSON KEY
                numbOfDotinOwnKey = GenericUtils.getInstance().countCharOccurrences(entry.getKey(), '.');
                
                //IF THE FIRST JSON KEY DOES NOT HAVE A DOT OR HAVE A DOT (FIRST LEVEL KEY OR NOT) -> SETS THE ORIGINAL ROOT JSON KEY
                if(entry.getKey().indexOf(".", numbOfDotinOwnKey) == -1) {
                    originalRootKey = entry.getKey();
                }else { 
                    originalRootKey = entry.getKey().substring(0, entry.getKey().indexOf(".", numbOfDotinOwnKey));
                }
            }
            
            //IF THE OTHER JSON KEY DOES NOT HAVE A DOT OR HAVE A DOT (FIRST LEVEL KEY OR NOT) -> THE RELATED JSON KEY IS NOT CHANGED OR CHANGED
            if(entry.getKey().indexOf(".", numbOfDotinOwnKey) == -1) {
                keyPathJsonToCheck = entry.getKey();
            }else{
                //IF THE JSON ORIGINAL ROOT KEY IS THE SAME OF THE RELATED JSON KEY OR NOT -> CUT THE ROOT KEY OR NOT
                if(entry.getKey().substring(0, entry.getKey().indexOf(".", numbOfDotinOwnKey)).equalsIgnoreCase(originalRootKey)) {
                    keyPathJsonToCheck = entry.getKey().substring(entry.getKey().indexOf(".",numbOfDotinOwnKey)+1, entry.getKey().length());
                }else {
                    keyPathJsonToCheck = entry.getKey();
                }
            }
            valuePathJsonToCheck = entry.getValue();
            keyValuePathJsonToCheckMODTempMap.put(keyPathJsonToCheck, valuePathJsonToCheck);
            i++;
        }
        //DISCARD ON THE FILTER MAP THE JSON KEY NOT PRESENT IN OWN LIST JSON OBJECT
        for(JSONObject obj : listOwnJson) {
            for (Object key : obj.keySet()) {
                String keyStr = (String)key;
                for (Map.Entry<String, String> entry : keyValuePathJsonToCheckMODTempMap.entrySet()){
                    if(entry.getKey().contains(keyStr)) {
                        keyValuePathJsonToCheckMODMap.put(entry.getKey(), entry.getValue());
                    }
                }
            }
        }
        return keyValuePathJsonToCheckMODMap;
    }
    
    
    /**
     * method for converts date from long timestamp to UTC date
     * @param timestamp date in format long timestamp
     * @return string date in format UTC
     */
    public String convertTimeStampToDateUTC(long timestamp) {
        Date date = new Date(timestamp);
        
        String ISO_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS zzz";
        
        SimpleDateFormat sdf = new SimpleDateFormat(ISO_FORMAT);
  
        TimeZone utc = TimeZone.getTimeZone("UTC");
        
        sdf.setTimeZone(utc);
        
        String lastDateTimeUTC = sdf.format(date).toString(); 
        
        return lastDateTimeUTC.replaceFirst(" UTC", "Z");
    }
    
    /**
	 * @param the input map with the values of the topic
	 *           
	 * @param  the value with the random value
	 *           
	 * @param the key with the random value
	 *            
	 * @return the map with the field with the updated random value
	 */
	public Map<String, String> fieldRandMap(Map<String, String> mapString,  String keyField,String randField) {
		Map<String, String> tempMap = new LinkedHashMap<String, String>();
		tempMap.put(keyField, randField);
		for (String key : mapString.keySet()) {
		 if (!key.equals(keyField))  
		        tempMap.put(key, mapString.get(key));
		}
		return tempMap;
	}
}
