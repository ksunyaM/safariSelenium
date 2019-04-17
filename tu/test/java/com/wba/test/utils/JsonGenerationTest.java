/*Copyright 2018 Walgreen Co.*/
package com.wba.test.utils;

import com.wba.test.utils.datacreation.JsonGenerator;
import com.wba.test.utils.kafka.Event;
import com.wba.test.utils.kafka.EventBuilder;
import com.wba.test.utils.kafka.EventStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.util.Collections;

public class JsonGenerationTest {
    private static Logger LOGGER = LoggerFactory.getLogger(JsonGenerationTest.class);
    private final String schema = "{\n" +
            "  \"type\": \"record\",\n" +
            "  \"name\": \"RxPatientPlanLoadRequest\",\n" +
            "  \"namespace\": \"com.wba.rxp.patientplan.load.event.avro\",\n" +
            "  \"doc\": \"Integration Layer sends rx Patient Plan Load Request to rxPatientplan\",\n" +
            "  \"fields\": [\n" +
            "    {\n" +
            "      \"name\": \"correlationId\",\n" +
            "      \"type\": {\n" +
            "        \"type\": \"string\",\n" +
            "        \"avro.java.string\": \"String\"\n" +
            "      }\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\": \"eventProducedTime\",\n" +
            "      \"type\": {\n" +
            "        \"type\": \"long\",\n" +
            "        \"logicalType\": \"timestamp-millis\"\n" +
            "      }\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\": \"patientCode\",\n" +
            "      \"type\": {\n" +
            "        \"type\": \"string\",\n" +
            "        \"avro.java.string\": \"String\"\n" +
            "      }\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\": \"patientPlans\",\n" +
            "      \"type\": [\n" +
            "        \"null\",\n" +
            "        {\n" +
            "          \"type\": \"array\",\n" +
            "          \"items\": {\n" +
            "            \"type\": \"record\",\n" +
            "            \"name\": \"PatientPlan\",\n" +
            "            \"fields\": [\n" +
            "              {\n" +
            "                \"name\": \"cardholderFirstName\",\n" +
            "                \"type\": {\n" +
            "                  \"type\": \"string\",\n" +
            "                  \"avro.java.string\": \"String\"\n" +
            "                }\n" +
            "              },\n" +
            "              {\n" +
            "                \"name\": \"cardholderLastName\",\n" +
            "                \"type\": {\n" +
            "                  \"type\": \"string\",\n" +
            "                  \"avro.java.string\": \"String\"\n" +
            "                }\n" +
            "              },\n" +
            "              {\n" +
            "                \"name\": \"planCode\",\n" +
            "                \"type\": {\n" +
            "                  \"type\": \"string\",\n" +
            "                  \"avro.java.string\": \"String\"\n" +
            "                }\n" +
            "              },\n" +
            "              {\n" +
            "                \"name\": \"generalRecipientNumber\",\n" +
            "                \"type\": {\n" +
            "                  \"type\": \"string\",\n" +
            "                  \"avro.java.string\": \"String\"\n" +
            "                }\n" +
            "              },\n" +
            "              {\n" +
            "                \"name\": \"groupNumber\",\n" +
            "                \"type\": [\n" +
            "                  \"null\",\n" +
            "                  {\n" +
            "                    \"type\": \"string\",\n" +
            "                    \"avro.java.string\": \"String\"\n" +
            "                  }\n" +
            "                ],\n" +
            "                \"default\": null\n" +
            "              },\n" +
            "              {\n" +
            "                \"name\": \"planExpirationDate\",\n" +
            "                \"type\": [\n" +
            "                  \"null\",\n" +
            "                  {\n" +
            "                    \"type\": \"int\",\n" +
            "                    \"logicalType\": \"date\"\n" +
            "                  }\n" +
            "                ],\n" +
            "                \"default\": null\n" +
            "              },\n" +
            "              {\n" +
            "                \"name\": \"primaryPlanIndicator\",\n" +
            "                \"type\": \"boolean\"\n" +
            "              },\n" +
            "              {\n" +
            "                \"name\": \"personCode\",\n" +
            "                \"type\": [\n" +
            "                  \"null\",\n" +
            "                  {\n" +
            "                    \"type\": \"string\",\n" +
            "                    \"avro.java.string\": \"String\"\n" +
            "                  }\n" +
            "                ],\n" +
            "                \"default\": null\n" +
            "              },\n" +
            "              {\n" +
            "                \"name\": \"patientUsualAndCustomaryIndicator\",\n" +
            "                \"type\": \"boolean\"\n" +
            "              },\n" +
            "              {\n" +
            "                \"name\": \"couponIndicator\",\n" +
            "                \"type\": \"boolean\"\n" +
            "              },\n" +
            "              {\n" +
            "                \"name\": \"cardholderRelationship\",\n" +
            "                \"type\": {\n" +
            "                  \"type\": \"enum\",\n" +
            "                  \"name\": \"CardholderRelationship\",\n" +
            "                  \"symbols\": [\n" +
            "                    \"SELF\",\n" +
            "                    \"SPOUSE\",\n" +
            "                    \"CHILD\",\n" +
            "                    \"OTHER\"\n" +
            "                  ]\n" +
            "                }\n" +
            "              }\n" +
            "            ]\n" +
            "          }\n" +
            "        }\n" +
            "      ],\n" +
            "      \"default\": null\n" +
            "    }\n" +
            "  ],\n" +
            "  \"version\": \"4.0.0\"\n" +
            "}";


    //    @Test
    public void generateJsonWithOnlyMandatoryFieldsTest() {
//        String json = new JsonGenerator()
//                .withAvroSchema(schema)
//                .withOnlyMandatoryFields()
//                .generateJsonFromAvro().toString();
//        LOGGER.info("Json with only mandatory fields: {}", JsonPath.with(json).prettify());
//        Assert.assertTrue(JsonUtils.getJsonValue(json, "patientPlans") instanceof NullNode);
    }

    //    @Test
    public void generateJsonWithAllFieldsTest() {
//        String json = new JsonGenerator()
//                .withAvroSchema(schema)
//                .generateJsonFromAvro().toString();
//        LOGGER.info("Json with all fields: {}", JsonPath.with(json).prettify());
//        Assert.assertTrue(JsonUtils.isJsonPathExists(json, "patientPlans.array"));
    }

    //    @Test
    public void generateJsonWithAllFieldsWith2ArraysTest() {
//        String json = new JsonGenerator()
//                .withAvroSchema(schema)
//                .withParameters(Collections.singletonMap("RxPatientPlanLoadRequest.patientPlans", 2))
//                .generateJsonFromAvro().toString();
//        LOGGER.info("Json with two elements in the array node: {}", JsonPath.with(json).prettify());
//        Assert.assertEquals(((ArrayNode) JsonUtils.getJsonValue(json, "patientPlans.array")).size(), 2);
    }

    //    @Test
    public void generateJsonWithAllFieldsWithDataTransformationTest() {
//        String json = new JsonGenerator()
//                .withAvroSchema(schema)
//                .withDataTransformation(this::fieldsData)
//                .generateJsonFromAvro().toString();
//        LOGGER.info("Json with data in the fields: {}", JsonPath.with(json).prettify());
//        Assert.assertEquals(((TextNode) JsonUtils.getJsonValue(json, "patientPlans.array[0].planCode")).asText(), "test_plan_code");
//        Assert.assertEquals(((TextNode) JsonUtils.getJsonValue(json, "patientCode")).asText(), "test_patient_code");
    }

//    @Test
    public void eventIsProducedWithJsonFromSchemaRegistry() {
        String topicName = "e2e-goods_receipt_submit";
        String schemaName = "GoodsReceiptSubmitted-value";
//        String body = new JsonGenerator().
//                withAvroSchema(ResourceUtils.getResourceAsString("schemas/NotifyRxProductCreateUpdate.avsc")).
//                generateJsonFromAvro().toString();
        String body = new JsonGenerator()
                .withAvroSchema(new SchemaRegistryUtils().getSchemaValue(schemaName))
                .generateJsonFromAvro().toString();
        Event event = new EventBuilder()
                .name("E")
                .topicName(topicName)
                .schemaName(schemaName)
                .body(body)
                .headers(Collections.singletonMap("user", "devUser"))
                .build();
        EventStorage.getInstance().addProduced(event);
    }

    private String fieldsData(String key) {
        switch (key) {
            case "location_number":
                return "59511";
            case "plan_code":
                return "test_plan_code";
            case "patient_code":
                return "test_patient_code";
            default:
                return key;
        }
    }

}
