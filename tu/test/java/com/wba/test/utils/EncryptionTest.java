/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils;

import com.jayway.jsonpath.JsonPath;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.junit.Assert.assertEquals;

public class EncryptionTest {

    // Test commented, as build machine does not have required certificates and library

    private final String CONFIGURATION_PATH = "src/test/resources/conf/local";
    private final String CLEAR_VALUE = "message";
    private final String ENCRYPTED_VALUE_KEY_ONE_CATEGORY_FPE_STRING = "ÖÆeD>ŢĜ";
    private final String ENCRYPTED_VALUE_KEY_TWO_CATEGORY_AES_STRING = "MIGvBgkqhkiG9w0BBwaggaEwgZ4CAQAwgZgGCSqGSIb3DQEHATB5BgpghkgBhv0eBQEDMGsEEGSdi6n6Ybg550biuvySl0UMV0VudF9QSUlAd2FsZ3JlZW5zOjA3MDEwMTAwMDAwMFo6ZGV2dGVzdC5wbXQud2FsZ3JlZW5zLmNvbSMxNDYzNzY4Mjc2OmRhdGE6QUVTLUNCQzoyNTY6OoAQWTU8/uYH9SMW/Y8q/1ZGGQ==";
    private final String ENCRYPTED_VALUE_KEY_THREE_CATEGORY_FPE_STRING = "{WŇÃ³+o";
    private final String ENCRYPTED_VALUE_KEY_FOUR_CATEGORY_AES_STRING = "MIGxBgkqhkiG9w0BBwaggaMwgaACAQAwgZoGCSqGSIb3DQEHATB7BgpghkgBhv0eBQEDMG0EEL/HA4q6wZeAaCyJ98VXy7cMWUVudF9QSUlfMUB3YWxncmVlbnM6MDcwMTAxMDAwMDAwWjpkZXZ0ZXN0LnBtdC53YWxncmVlbnMuY29tIzE0NjM3NjgyNzY6ZGF0YTpBRVMtQ0JDOjI1Njo6gBCf5sGWWOn+m8K5yfGXUzZD";
    private final String REPLACE_PATTERN_KEY = "?k";
    private final String REPLACE_PATTERN_VALUE = "?v";
    private final String PATH_FIELD = "field.name";
    private final String PATH_VALUE = "$['" + PATH_FIELD + "']['value']";
    private String jsonBody = "{\"" + PATH_FIELD + "\":{\"keyId\":\"" + REPLACE_PATTERN_KEY + "\"," +
            "\"value\":\"" + REPLACE_PATTERN_VALUE + "\"}}";

    @DataProvider(name = "encryptJson")
    public Object[][] encryptJson() {
        return new Object[][]{
                {CLEAR_VALUE, "0", CLEAR_VALUE},
                {CLEAR_VALUE, "1", ENCRYPTED_VALUE_KEY_ONE_CATEGORY_FPE_STRING},
                // encryption with key 2 could not be tested, as actual encrypted message always different
                {CLEAR_VALUE, "3", ENCRYPTED_VALUE_KEY_THREE_CATEGORY_FPE_STRING}
                // encryption with key 4 could not be tested, as actual encrypted message always different
        };
    }

    @DataProvider(name = "decryptJson")
    public Object[][] decryptJson() {
        return new Object[][]{
                {CLEAR_VALUE, "0", CLEAR_VALUE},
                {ENCRYPTED_VALUE_KEY_ONE_CATEGORY_FPE_STRING, "1", CLEAR_VALUE},
                {ENCRYPTED_VALUE_KEY_TWO_CATEGORY_AES_STRING, "2", CLEAR_VALUE},
                {ENCRYPTED_VALUE_KEY_THREE_CATEGORY_FPE_STRING, "3", CLEAR_VALUE},
                {ENCRYPTED_VALUE_KEY_FOUR_CATEGORY_AES_STRING, "4", CLEAR_VALUE}
        };
    }

    @DataProvider(name = "encryptValue")
    public Object[][] encryptValue() {
        return new Object[][]{
                {PATH_FIELD, CLEAR_VALUE, null, "CLEAR_TEXT", CLEAR_VALUE},
                {PATH_FIELD, CLEAR_VALUE, "0", null, CLEAR_VALUE},
                {PATH_FIELD, CLEAR_VALUE, "0", "CLEAR_TEXT", CLEAR_VALUE},
                {PATH_FIELD, CLEAR_VALUE, null, "PII_FPE_STRING", ENCRYPTED_VALUE_KEY_ONE_CATEGORY_FPE_STRING},
                {PATH_FIELD, CLEAR_VALUE, "1", null, ENCRYPTED_VALUE_KEY_ONE_CATEGORY_FPE_STRING},
                {PATH_FIELD, CLEAR_VALUE, "1", "PII_FPE_STRING", ENCRYPTED_VALUE_KEY_ONE_CATEGORY_FPE_STRING},
                // encryption with category PII_AES_STRING or key 2 could not be tested, as actual encrypted message always different
                // encryption with category PII_FPE_STRING and inactive key (3) is not possible w/o specifying key
                {PATH_FIELD, CLEAR_VALUE, "3", null, ENCRYPTED_VALUE_KEY_THREE_CATEGORY_FPE_STRING},
                {PATH_FIELD, CLEAR_VALUE, "3", "PII_FPE_STRING", ENCRYPTED_VALUE_KEY_THREE_CATEGORY_FPE_STRING}
                // encryption with category PII_AES_STRING or key 4 could not be tested, as actual encrypted message always different
        };
    }

    @DataProvider(name = "decryptValue")
    public Object[][] decryptValue() {
        return new Object[][]{
                {PATH_FIELD, CLEAR_VALUE, null, "CLEAR_TEXT", CLEAR_VALUE},
                {PATH_FIELD, CLEAR_VALUE, "0", null, CLEAR_VALUE},
                {PATH_FIELD, CLEAR_VALUE, "0", "CLEAR_TEXT", CLEAR_VALUE},
                {PATH_FIELD, ENCRYPTED_VALUE_KEY_ONE_CATEGORY_FPE_STRING, null, "PII_FPE_STRING", CLEAR_VALUE},
                {PATH_FIELD, ENCRYPTED_VALUE_KEY_ONE_CATEGORY_FPE_STRING, "1", null, CLEAR_VALUE},
                {PATH_FIELD, ENCRYPTED_VALUE_KEY_ONE_CATEGORY_FPE_STRING, "1", "PII_FPE_STRING", CLEAR_VALUE},
                {PATH_FIELD, ENCRYPTED_VALUE_KEY_TWO_CATEGORY_AES_STRING, null, "PII_AES_STRING", CLEAR_VALUE},
                {PATH_FIELD, ENCRYPTED_VALUE_KEY_TWO_CATEGORY_AES_STRING, "2", null, CLEAR_VALUE},
                {PATH_FIELD, ENCRYPTED_VALUE_KEY_TWO_CATEGORY_AES_STRING, "2", "PII_AES_STRING", CLEAR_VALUE},
                // decryption with category PII_FPE_STRING and inactive key (3) is not possible w/o specifying key
                {PATH_FIELD, ENCRYPTED_VALUE_KEY_THREE_CATEGORY_FPE_STRING, "3", null, CLEAR_VALUE},
                {PATH_FIELD, ENCRYPTED_VALUE_KEY_THREE_CATEGORY_FPE_STRING, "3", "PII_FPE_STRING", CLEAR_VALUE},
                {PATH_FIELD, ENCRYPTED_VALUE_KEY_FOUR_CATEGORY_AES_STRING, null, "PII_AES_STRING", CLEAR_VALUE},
                {PATH_FIELD, ENCRYPTED_VALUE_KEY_FOUR_CATEGORY_AES_STRING, "4", null, CLEAR_VALUE},
                {PATH_FIELD, ENCRYPTED_VALUE_KEY_FOUR_CATEGORY_AES_STRING, "4", "PII_AES_STRING", CLEAR_VALUE}
        };
    }

    private void setSystemProperties() {
        System.setProperty("confPath", CONFIGURATION_PATH);
    }

//    @Test(dataProvider = "encryptJson")
    public void encryptJsonTest(String clearValue, String key, String expectedEncryptedValue) {
        setSystemProperties();
        String actualEncryptedValue = JsonPath.read(new EncryptionUtils().encryptJson(jsonBody
                .replace(REPLACE_PATTERN_KEY, key).replace(REPLACE_PATTERN_VALUE, clearValue)), PATH_VALUE);
        assertEquals(expectedEncryptedValue, actualEncryptedValue);
    }

//    @Test(dataProvider = "decryptJson")
    public void decryptJsonTest(String encryptedValue, String key, String expectedClearValue) {
        setSystemProperties();
        String actualClearValue = JsonPath.read(new EncryptionUtils().decryptJson(jsonBody
                .replace(REPLACE_PATTERN_KEY, key).replace(REPLACE_PATTERN_VALUE, encryptedValue)), PATH_VALUE);
        assertEquals(expectedClearValue, actualClearValue);
    }

//    @Test(dataProvider = "encryptValue")
    public void encryptValueTest(String field, String clearValue, String key, String category, String expectedEncryptedValue) {
        setSystemProperties();
        String actualEncryptedValue = new EncryptionUtils().encryptValue(field, clearValue, key, category);
        assertEquals(expectedEncryptedValue, actualEncryptedValue);
    }

//    @Test(dataProvider = "decryptValue")
    public void decryptValueTest(String field, String encryptedValue, String key, String category, String expectedClearValue) {
        setSystemProperties();
        String actualClearValue = new EncryptionUtils().decryptValue(field, encryptedValue, key, category);
        assertEquals(expectedClearValue, actualClearValue);
    }
}
