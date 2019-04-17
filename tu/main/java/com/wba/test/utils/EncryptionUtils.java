/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.Predicate;
import com.wba.test.utils.kafka.Event;
import org.slf4j.Logger;

import java.util.*;

import static com.oneleo.test.automation.core.EncryptionFieldLevelUtils.efl;
import static com.oneleo.test.automation.core.LogUtils.log;

public class EncryptionUtils {
    private static final Logger LOGGER = log(EncryptionUtils.class);
    private final String PATH_KEY_ID = "keyId";
    private final String PATH_VALUE = "value";
    private final String KEY_ID = "keyid";
    private final String FIELD = "field";
    private final String CATEGORY = "category";
    private final String CLEAR_VALUE = "clear_value";
    private final String ENCRYPTED_VALUE = "encrypted_value";
    private final String KEY_ZERO = "0";
    private final String CATEGORY_CLEAR_TEXT = "CLEAR_TEXT";
    private String resourceName;

    public EncryptionUtils() {
        this("default");
    }

    public EncryptionUtils(String resourceName) {
        this.resourceName = resourceName;
    }

    private Set<String> getAllEncryptedPaths(String jsonBody) {
        Configuration config = Configuration.builder().options(Option.AS_PATH_LIST).build();
        List<String> pathMatchers = JsonPath.using(config).parse(jsonBody).read("$..keyId", new Predicate[0]);
        return new HashSet<>(pathMatchers);
    }

    public void encryptEvent(Event event) {
        event.setBody(encryptJson(event.getBody()));
    }

    public String encryptJson(String jsonBody) {
        Set<String> pathsToEncrypt = getAllEncryptedPaths(jsonBody);
        for (String path : pathsToEncrypt) {
            String key = JsonPath.read(jsonBody, path);
            String clearValue = JsonPath.read(jsonBody, path.replace(PATH_KEY_ID, PATH_VALUE));
            String encryptedValue = encryptValue(path, clearValue, key, null);
            LOGGER.info("Field with path {} encrypted as {}", path, encryptedValue);
            jsonBody = JsonPath.parse(jsonBody).set(path.replace(PATH_KEY_ID, PATH_VALUE), encryptedValue).jsonString();
        }
        return jsonBody;
    }

    public String encryptValue(String field, String clearValue, String key, String category) {
        if ((key != null && key.equals(KEY_ZERO)) || (category != null && category.equals(CATEGORY_CLEAR_TEXT))) {
            LOGGER.info("Key is 0 for field: '" + field + "', encryption skipped");
            return clearValue;
        } else {
            Map<String, String> toBeEncrypted = new HashMap<String, String>() {{
                if (key != null) {
                    put(KEY_ID, key);
                }
                if (category != null) {
                    put(CATEGORY, category);
                }
                put(FIELD, field);
                put(CLEAR_VALUE, clearValue);
            }};
            return efl().encryptFieldValue(resourceName, Collections.singletonList(toBeEncrypted)).get(0).get(ENCRYPTED_VALUE);
        }
    }

    public void decryptEvent(Event event) {
        event.setBody(decryptJson(event.getBody()));
    }

    public String decryptJson(String jsonBody) {
        Set<String> pathsToDecrypt = getAllEncryptedPaths(jsonBody);
        for (String path : pathsToDecrypt) {
            String key = JsonPath.read(jsonBody, path);
            String encryptedValue = JsonPath.read(jsonBody, path.replace(PATH_KEY_ID, PATH_VALUE));
            String clearValue = decryptValue(path, encryptedValue, key, null);
            LOGGER.info("Field with path {} decrypted as {}", path, clearValue);
            jsonBody = JsonPath.parse(jsonBody).set(path.replace(PATH_KEY_ID, PATH_VALUE), clearValue).jsonString();
        }
        return jsonBody;
    }

    public String decryptValue(String field, String encryptedValue, String key, String category) {
        if ((key != null && key.equals(KEY_ZERO)) || (category != null && category.equals(CATEGORY_CLEAR_TEXT))) {
            LOGGER.info("Key is 0 for field: '" + field + "', decryption skipped");
            return encryptedValue;
        } else {
            Map<String, String> toBeDecrypted = new HashMap<String, String>() {{
                if (key != null) {
                    put(KEY_ID, key);
                }
                if (category != null) {
                    put(CATEGORY, category);
                }
                put(FIELD, field);
                put(ENCRYPTED_VALUE, encryptedValue);
            }};
            return efl().decryptFieldValue(resourceName, Collections.singletonList(toBeDecrypted)).get(0).get(CLEAR_VALUE);
        }
    }
}
