/*
 * Copyright 2018 Walgreen Co.
 */

/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.wba.test.utils.kafka.EventBuilder;
import com.wba.test.utils.kafka.EventStorage;
import org.apache.commons.configuration.ConfigurationException;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.*;

import static com.wba.test.utils.JsonUtils.setJsonAttribute;
import static org.testng.Assert.*;


public class JsonUtilsTest {

    private final String eventJson01 = "{\n" +
            "  \"correlationId\":\"ok\",\n" +
            "  \"eventProducedTime\":0,\n" +
            "  \"claimNumber\":\"x\",\n" +
            "  \"locationIdentifier\": {\n" +
            "    \"locationNumber\": \"x\",\n" +
            "    \"locationType\": \"x\"\n" +
            "  },\n" +
            "  \"returnAuthorizationNumber\": \"x\",\n" +
            "  \"approvedItems\": {\n" +
            "    \"array\" :  [\n" +
            "      {\n" +
            "        \"rejectedQuantityInPacks\": 9999,\n" +
            "        \"rejectedReason\": \"NOT_RECEIVED\",\n" +
            "        \"upc\": \"x\"\n" +
            "      }\n" +
            "    ]\n" +
            "  }\n" +
            "\n" +
            "}";

    private final String complexJson = "{\n" +
            "    \"key01\": [\n" +
            "        {\n" +
            "            \"key11\": 11\n" +
            "        },\n" +
            "        {\n" +
            "            \"key12\": 12\n" +
            "        }\n" +
            "    ],\n" +
            "    \"key00\": {\n" +
            "        \"key12\": \"\",\n" +
            "        \"key11\": null\n" +
            "    }\n" +
            "}";

    @DataProvider(name = "test2")
    public Object[][] data2() {
        return new Object[][]{
                // update
                {"key01", "key level 1"},
                {"key01[1].key12", "array key"},
                {"key00.key11", "key key"},
                {"key00.key11.new", "key key new key"},
                // add
                {"key1", "new key level 1 "},
                {"key.new", "new key key"},
                {"key[0].key12", "new array new key"},
                {"key02.key11[0].key01", "new key new array new key"},
                {"key01[2].new", "new array element"},
                {"key00.new", "key new key"},
        };
    }

    @DataProvider(name = "prettify")
    public Object[][] prettify() {
        return new Object[][]{
                {null, "@jiraid=123 @smoke", true},
                {null, "@jiraid=123 @smoke prettify", true},
                {null, "@jiraid=123 @smoke @prEttify", true},
                {null, "@jiraid=123 @smoke @prettify", true},
                {null, "@jiraid=123 @prettify @smoke", true},
                {null, "@prettify @jiraid=123 @smoke", true},
                {null, "@jiraid=123 @smoke noprettify", true},
                {null, "@jiraid=123 @smoke @noprEttify", false},
                {null, "@jiraid=123 @smoke @noprettify", false},
                {null, "@jiraid=123 @noprettify @smoke", false},
                {null, "@noprettify @jiraid=123 @smoke", false},
                {false, "@jiraid=123 @smoke", true},
                {false, "@jiraid=123 @smoke prettify", true},
                {false, "@jiraid=123 @smoke @prEttify", true},
                {false, "@jiraid=123 @smoke @prettify", true},
                {false, "@jiraid=123 @prettify @smoke", true},
                {false, "@prettify @jiraid=123 @smoke", true},
                {false, "@jiraid=123 @smoke noprettify", true},
                {false, "@jiraid=123 @smoke @noprEttify", false},
                {false, "@jiraid=123 @smoke @noprettify", false},
                {false, "@jiraid=123 @noprettify @smoke", false},
                {false, "@noprettify @jiraid=123 @smoke", false},
                {true, "@jiraid=123 @smoke", true},
                {true, "@jiraid=123 @smoke prettify", true},
                {true, "@jiraid=123 @smoke @prEttify", true},
                {true, "@jiraid=123 @smoke @prettify", true},
                {true, "@jiraid=123 @prettify @smoke", true},
                {true, "@prettify @jiraid=123 @smoke", true},
                {true, "@jiraid=123 @smoke noprettify", true},
                {true, "@jiraid=123 @smoke @noprEttify", false},
                {true, "@jiraid=123 @smoke @noprettify", false},
                {true, "@jiraid=123 @noprettify @smoke", false},
                {true, "@noprettify @jiraid=123 @smoke", false}
        };
    }

    @Test
    public void emptyJsonTest() {
        assertEquals(0, ((ArrayNode) JsonUtils.getJsonValue("{\"q\" : {}}", "q..*")).size());
    }


    @Test(dataProvider = "test2")
    public void setJsonTest(String key, Object value) {
        String newJson = setJsonAttribute(complexJson, key, value);
        assertEquals(JsonUtils.getJsonValue(newJson, key).toString(), "\"" + value + "\"");
    }

    @Test
    public void jsonExistsTest() {
        assertTrue(JsonUtils.isJsonPathExists(complexJson, "key00.key11"));
        assertTrue(JsonUtils.isJsonPathExists(complexJson, "key01[0]"));
        assertTrue(JsonUtils.isJsonPathExists(complexJson, "key01[0].key11"));
        assertFalse(JsonUtils.isJsonPathExists(complexJson, "key"));
        assertFalse(JsonUtils.isJsonPathExists(complexJson, "key01[2]"));
    }

    @Test
    public void eventJsonUpdate01() {

        final HashMap<String, String> data = new HashMap<String, String>() {{
            put("claimNumber", "ok");
            put("locationIdentifier.locationNumber", "ok");
            put("locationIdentifier.locationType", "ok");
            put("returnAuthorizationNumber", "ok");
            for (int i = 0; i < 2; i++) {
                put("approvedItems.array[" + i + "].rejectedQuantityInPacks", "1111");
                put("approvedItems.array[" + i + "].rejectedReason", "ok");
                put("approvedItems.array[" + i + "].upc", "ok");
            }
        }};

        final String json = JsonUtils.prettify(new BaseStep().updateJson(eventJson01, data));
        final List<String[]> matches = RegExp.getMatches(json, "(\"x\")");
        assertEquals(matches.size(), 0);
        assertEquals(RegExp.getMatches(json, "9999").size(), 0);
    }

    @Test
    public void compareJsonTestEqual() {
        assertEquals(JsonUtils.compareJSONs(eventJson01, eventJson01), "[]");
    }

    @Test
    public void readUTF() {
        String jsonUTF = "{\n" +
                "    \"directLinkCode\": \"NDC\",\n" +
                "    \"adjudicationMessage\": \"011559D0B1ILDR      114PLANASSIGNED1  20180410CID       \u001E\u001CAM01\u001CC420010513\u001CC52\u001CCADANIEL\u001CCBNAFTE\u001C1C1\u001C2C2\u001CCM300 BERSHIRE LANE\u001CCNBUFFALO GROVE\u001CCOAK\u001CCP80121\u001CCQ3128689905\u001CC70\u001CCX  \u001CCY                    \u001C4X1\u001E\u001CAM04\u001CC287565               \u001CCCDAVIS\u001CCDPORUNNEDAM\u001CC90\u001CC12000177\u001CC3583\u001CC61\u001C2A                    \u001D\u001E\u001CAM07\u001CEM1\u001CD2123456\u001CE103\u001CD7NDC5672\u001CE70000000000\u001CD34\u001CD50\u001CD61\u001CDF0\u001CDJ5\u001CDT0\u001CD8S\u001CET0000000000\u001CDE20180321\u001C28GM\u001CEK            \u001CE2372449004\u001CU71\u001E\u001CAM03\u001CEZ01\u001CDBSTRING\u001CDRSTRING\u001C2JSTRING\u001C2KSTRING\u001C2MSTRING\u001C2NAK\u001C2PSTRING\u001C2E  \u001CDL               \u001CPMSTRING\u001C4E               \u001E\u001CAM11\u001CD90000000{\u001CDC0000000{\u001CDX0000022C\u001CE30000000{\u001CHA0000000{\u001CGE0000000{\u001CHE000000{\u001CJE02\u001CDQ0000000{\u001CDU0000000{\u001CDN01\u001E\u001CAM02\u001CEY04\u001CE9FIRSTNAME, LAST\",\n" +
                "    \"correlationId\": \"6178bbe5-43ce-46e6-95b2-dfedfb701424\"\n" +
                "    }\n" +
                "}";
        assertNotNull(JsonUtils.readJson(jsonUTF, LinkedHashMap.class));
    }

    @Test(dataProvider = "prettify")
    public void prettifyTest(Boolean prettifyOption, String tags, boolean assertion) throws ConfigurationException {
        System.setProperty("confPath", "src/test/resources/conf/local");
        if (prettifyOption != null) {
            if (prettifyOption) {
                Map<String, String> data = new HashMap<>();
                data.put("prettify", prettifyOption.toString());

                PropertiesReader.updateProperties(System.getProperty("user.dir") +
                        "/target/test-classes/conf/local/default-api.properties", data);
            }
        }

        EventStorage eventStorage = EventStorage.getInstance();
        DataStorage dataStorage = DataStorage.getInstance();
        dataStorage.map().put("~SCENARIO_TAGS", tags);
        eventStorage.addProduced(new EventBuilder().name("NO_PRETTIFY")
                .body("{\"correlationId\": \"79d6d812-e1c2-47f3-acf4-73c6ec545ea6\"," +
                        "\"eventProducedTime\": \"1493643600000\"}")
                .build());
        assertEquals(eventStorage.getLastProduced().getBody().contains("\n"), assertion,
                String.format("Body is %sprettified:", assertion ? "not " : ""));

        EventStorage.reset();
        dataStorage.reset();

        if (prettifyOption != null) {
            if (prettifyOption) {
                PropertiesReader.clearProperties(System.getProperty("user.dir") +
                        "/src/test/resources/conf/local/default-api.properties", Collections.singletonList("prettify"));
            }
        }
    }

    @Test
    public void TestNullToArray() {
        String json = "{\"a\" : null}";
        Assert.assertEquals(JsonUtils.setJsonAttribute(json, "a[0]", "1"), "{\"a\":[\"1\"]}");
    }


//    @Test
//    public void tnode() {
//        final BaseStep bs = new BaseStep();
//        bs.eventStorage.addProduced(new EventBuilder().body(eventJson01).build());
//
//        String jsonPath = "P.approvedItems.array";
//        final String[] n = RegExp.getMatches(jsonPath, "^([^.]+)\\.(.*)").get(0);
//
//        String json = bs.eventStorage.findEvent(n[0]).getBody();
//
//        final Integer size = bs.asInt(jsonPath + ".length()");
//        final ObjectNode node = (ObjectNode) bs.defineValue(jsonPath + "[0]");
//        json = setJsonAttribute(json, jsonPath + "[" + size + "]", node);
//        bs.eventStorage
//
//    }
}
