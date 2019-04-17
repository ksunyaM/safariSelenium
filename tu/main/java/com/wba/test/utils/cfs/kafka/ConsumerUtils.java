/* Copyright 2018 Walgreen Co.*/
package com.wba.test.utils.cfs.kafka;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wba.test.utils.cfs.utilities.GenericUtils;
import com.wba.test.utils.cfs.utilities.JsonUtils;

public class ConsumerUtils {
    
    private static ConsumerUtils instance;
    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerUtils.class);

    private ConsumerUtils()
    {
    }
    /**
     * method for create and get a instance of Consumer class
     * @return
     * the related created instance
     */
    public static ConsumerUtils getInstance()
    {
        if (instance == null)
        {
            instance = new ConsumerUtils();
        }
        return instance; 
    }
   
   /**
    *method for create and subscription kafka consumer
    *@param prop is the properties for the Kafka Consumer 
    *@param topicNotifyName is the topic for the subscription of the consumer
    */
   public KafkaConsumer<String, String> createAndSubscriptionConsumer(Properties prop, String topicNotifyName){
       KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<String, String>(prop);
       kafkaConsumer.subscribe(Collections.singletonList(topicNotifyName));
       
       return kafkaConsumer;
   }
   
   /**
    *method for read a event msg on the specific topic
    *@param prop is the properties for the Kafka Consumer 
    *@param kafkaConsumer is the consumer created and submitted on the specific topic
    */
   public List<JSONObject> listRecordJsonConsumed(Properties prop, KafkaConsumer<String, String> kafkaConsumer){

       int giveUp = Integer.valueOf(prop.getProperty("poll.iteration"));
       int noRecordsCount = 0;
       List<JSONObject> listRecordJsonConsumed = new ArrayList<JSONObject>();

       while (true) {
           final ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(1000);

           if (consumerRecords.count()==0) {
               noRecordsCount++;
               if (noRecordsCount > giveUp) break;
               else continue;
           }
           consumerRecords.forEach(record -> {
               JSONObject newJson = new JSONObject(((Object)record.value()).toString());
               listRecordJsonConsumed.add(newJson);

           });
           kafkaConsumer.commitAsync();
       }
       kafkaConsumer.close();
       
       return listRecordJsonConsumed;
   }
   
   /**
    *method for load and get the file properties for the create the consumer kafka
    *@param urlProperties is the path of the default properties of the kafka 
    */
   public Properties getProperties(String urlProperties) {
       // read properties file
       Properties prop = new Properties();
       InputStream is = this.getClass().getClassLoader().getResourceAsStream(urlProperties);

       // load a properties file
       try {
           prop.load(is);
           is.close();
       } catch (IOException e) {
           LOGGER.error("PROPERTIES FILE NOT FOUND: " + urlProperties);
           throw new RuntimeException(e);
       }
       
       prop.put(ConsumerConfig.GROUP_ID_CONFIG, UUID.randomUUID().toString());
       
       return prop;
   }
   
   /**
    *method for read a event msg on the specific topic
    *@param urlProperties is the path of the default properties of the kafka 
    *@param topicNotifyName is the topic for the subscription of the consumer
    *@return a list of JSON Object with a all message on the related topic
    */
   public List<JSONObject> consumerEvo(String urlProperties, String topicNotifyName){
       //LOAD THE PROPERTIES FOR THE CONSUMER
       Properties prop = ConsumerUtils.getInstance().getProperties(urlProperties);
       
       //CREATE AND SUBSCRIBE THE CONSUMER WITH THE SPECIFIC PROPERTIES AND THE SPECIFIC TOPIC
       KafkaConsumer<String, String> kafkaConsumer = ConsumerUtils.getInstance().createAndSubscriptionConsumer(prop, topicNotifyName);
       
       //CONSUME THE EVENT WITH OWN CONSUMER CREATED
       List<JSONObject> listRecordJsonConsumed = ConsumerUtils.getInstance().listRecordJsonConsumed(prop, kafkaConsumer);
       
       LOGGER.info("TOPIC: " + topicNotifyName);
       LOGGER.info("PATH KAFKA PROP: " + urlProperties);
       LOGGER.info("LIST TOPIC MESSAGE EVENT: " + listRecordJsonConsumed.toString());
             
       return listRecordJsonConsumed;
   }
   
   /**
    *method for read a event msg on the specific topic and check the field of the json
    *@param urlProperties urlProperties is the path of the default properties of the kafka
    *@param topicNotifyName is the topic for the subscription of the consumer
    *@param keyValuePathJsonToCheckMap  map with a path of the specific field and the related value
    *@return true if the message is found
    */
   public Boolean consumerEvoWithCheckJsonField(String urlProperties, String topicNotifyName, Map<String, String> keyValuePathJsonToCheckMap) {
       Map<String, String> keyValuePathJsonToCheckMODMap = new HashMap<String, String>();
       
       List<JSONObject> listRecordJsonConsumed = ConsumerUtils.getInstance().consumerEvo(urlProperties, topicNotifyName);
       
       if(listRecordJsonConsumed.isEmpty()) {
           return false;
       }
       
       LOGGER.info("CHECK MAP: " + keyValuePathJsonToCheckMap);
       
       //EXTRACT THE OWN JSON OBJECT LIST FROM THE FULL JSON OBJECT LIST BY FILTER MAP
       List<JSONObject> listOwnJson = JsonUtils.getInstance().getJsonObjMessageByJsonKey(keyValuePathJsonToCheckMap, listRecordJsonConsumed);
       
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
