/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils;


public class EnvTest {
//    private static final org.slf4j.Logger LOGGER = log(LogUtils.class);
//    private static final String CONF_FILE= "src/test/resources/conf/local/default-kafka.properties";
//    private static String DEFAULT_KAFKA = "bootstrap.servers=10.217.36.54:9092,10.217.36.55:9092,10.217.36.56:9092\n" +
//            "schema.registry.url=http://10.217.36.53:8081\n" +
//            "value.serializer=io.confluent.kafka.serializers.KafkaAvroSerializer\n" +
//            "key.serializer=org.apache.kafka.common.serialization.StringSerializer\n" +
//            "key.deserializer=org.apache.kafka.common.serialization.StringDeserializer\n" +
//            "value.deserializer=io.confluent.kafka.serializers.KafkaAvroDeserializer\n" +
//            "serialization.type=avro";
//
//    @DataProvider(name = "envTopicName")
//    public Object[][] data01() {
//        return new Object[][]{
//                {"intake_scheduledrefill_request", "devqe-intake_scheduledrefill_request"},
//                {"~intake_scheduledrefill_request", "intake_scheduledrefill_request"}
//        };
//    }
//
//
//    @Test(dataProvider = "envTopicName")
//    public void envTopicNameTest(String topicName, String envTopicName) {
//        System.setProperty("confPath", "src/test/resources/conf/local");
//        try(FileWriter propertyFile = new FileWriter(CONF_FILE, true)){
//            propertyFile.write("kafka.topic.environment.name=devqe");
//        } catch (Exception e) {
//            LOGGER.info(e.toString());
//        }
//        Assert.assertEquals(EnvironmentNameTransformUtils.transformName(topicName), envTopicName);
//    }
//
//    @DataProvider(name = "envTest")
//    public Object[][] data02() {
//        return new Object[][]{
//                {"", "intake_scheduledrefill_request"},
//                {"kafka.topic.environment.name=", "intake_scheduledrefill_request"}
//        };
//    }
//
//    @Test(dataProvider = "envTest")
//    public void envTopicNameFailTest(String propertyValue, String result) {
//        try(FileWriter propertyFile = new FileWriter(CONF_FILE, true)){
//            propertyFile.write(propertyValue);
//        } catch (Exception e) {
//            LOGGER.info(e.toString());
//        }
//        Assert.assertEquals(EnvironmentNameTransformUtils.transformName("intake_scheduledrefill_request"), result);
//    }
//
//    @BeforeMethod()
//    public void saveKafkaPropertyFile() throws IOException {
//        File source = new File(CONF_FILE);
//        if (source.createNewFile() || source.exists()) {
//            try(FileWriter writer = new FileWriter(source)){
//                writer.write(DEFAULT_KAFKA);
//                writer.write("\n");
//            } catch (Exception e) {
//                LOGGER.info(e.toString());
//            }
//        }
//    }
//
//    @AfterClass
//    public void clean() throws IOException {
//        File destination = new File(CONF_FILE);
//        try(FileWriter writer = new FileWriter(destination)) {
//            writer.write("");
//            writer.close();
//            destination.deleteOnExit();
//        } catch (Exception e) {
//            LOGGER.info(e.toString());
//        }
//    }
}
