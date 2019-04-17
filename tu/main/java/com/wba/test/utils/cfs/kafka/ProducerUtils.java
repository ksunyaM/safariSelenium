/* Copyright 2018 Walgreen Co.*/
package com.wba.test.utils.cfs.kafka;

import static com.oneleo.test.automation.core.MessageUtils.kafka;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeaders;

import com.oneleo.test.automation.core.kafka.AvroProducerRecordUtils;

import io.confluent.kafka.schemaregistry.client.rest.exceptions.RestClientException;

public class ProducerUtils {

    private static ProducerUtils instance;
    private static Map<String, String> headerMap = new HashMap<>();
    private static RecordHeaders headers;
    private static ProducerRecord<Object, Object> record;

    private ProducerUtils()
    {
    }
    /**
     * method for create and get a instance of Producer class
     * @return
     * the related created instance
     */
    public static ProducerUtils getInstance()
    {
        if (instance == null)
        {
            instance = new ProducerUtils();
        }
        return instance; 
    }
    /**
     * method for create a headers for the event
     * @param destination capability event
     * @param name name of the owner team
     * @param user name of the test launch environment 
     * @return
     * the related created headers
     */
    public RecordHeaders createHeaders(String destination, String name, String user){
        headerMap.put("dynamic.event.destination", destination);
        headerMap.put("ef.name", name);
        headerMap.put("ef.user", user);
        headers = AvroProducerRecordUtils.createRecordHeaders(headerMap);
        return headers;
    }
    
    /**
     * method for send a event on the related topic
     */
    public void sendEvent(String topicName, String schemaName, String producerEventNotifyCreateLocationJson, RecordHeaders headers, int kafkaPartition, String eventKeyString){
        try {
            record = AvroProducerRecordUtils.createAvroRecordWithHeaders(topicName, schemaName, producerEventNotifyCreateLocationJson, headers, kafkaPartition, eventKeyString);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        kafka().template().send(record);
    }
    /**
     * method for send a event on the related topic
     */
    public void sendEvent(EventUtils event, String producerEventNotifyCreateLocationJson){
        try {
            record = AvroProducerRecordUtils.createAvroRecordWithHeaders(event.getTopicName(),event.getSchemaName(), producerEventNotifyCreateLocationJson, event.getHeaders(),event.getPartition(), event.getEventKey());
        } catch (IOException  e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        kafka().template().send(record);
    }
}
