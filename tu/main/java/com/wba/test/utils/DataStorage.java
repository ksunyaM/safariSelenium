/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils;

import com.wba.test.utils.kafka.LogUtils;
import cucumber.api.DataTable;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.oneleo.test.automation.core.LogUtils.log;

@SuppressWarnings("WeakerAccess")
public class DataStorage {
    private static DataStorage ourInstance = new DataStorage();

    private static final Logger LOGGER = log(LogUtils.class);

    public static DataStorage getInstance() {
        return ourInstance;
    }

    private LinkedHashMap<String, Object> storage = new LinkedHashMap<>();

    private DataStorage() {
    }

    public Map<String, Object> map() {
        return storage;
    }

    public DataStorage add(String key, Object value) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(key, value);
        return add(map);

    }

    public DataStorage add(Map<String, Object> data) {
        if (data == null || data.size() == 0) {
            throw new RuntimeException("Data storage: map does not contain data");
        }
        data.forEach((s, o) -> {
            if (storage.containsKey(s)) {
                throw new RuntimeException("Element already present for key: " + s + " " +
                        o.toString() + " / " + storage.get(s).toString());
            }
            storage.put(s, o);
            LOGGER.debug("Data added: {} => {}", s, o);
        });
        return getInstance();
    }

    /**
     * Add new entry with specified key + number. For new element number will be 0.
     *
     * @param key   string
     * @param value object
     * @return the new key;
     */
    public String addWithNumber(String key, Object value) {
        final String newKey = key + getNextNumber(key);
        add(newKey, value);
        return newKey;
    }

    public Object unmask(String key) {
        return storage.getOrDefault(key, key);
    }

    public Map<String, Object> unmaskAsMap(DataTable data) {
        Map<String, Object> map = new HashMap<>();
        data.asMap(String.class, Object.class)
                .forEach((k, v) -> map.put(unmask(k).toString(), unmask(v.toString()))
                );
        return map;
    }

    public boolean has(String key) {
        return map().containsKey(key);
    }

    //TODO delete after 11/18
    @Deprecated
    public int getNextDataStorageNumber(String name) {
        return getNextNumber(name);
    }

    /**
     * Return next number for given element e.g.: when dataStorage has keys "EL0", "EL1", than getNextNumber("EL") returns 3.
     * If key is not exist - returns 0.
     *
     * @param name - key in dataStorage (w/o number)
     * @return number for next element for specified key
     */
    public int getNextNumber(String name) {
        int num = 0;
        while (map().containsKey(name + num)) {
            num++;
        }
        return num;
    }

    public void reset() {
        storage = new LinkedHashMap<>();
    }
}
