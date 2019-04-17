/* Copyright 2018 Walgreen Co.*/
package com.wba.test.utils.cfs.kafka;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oneleo.test.automation.core.kafka.AvroProducerRecordUtils;
import com.oneleo.test.automation.core.kafka.KafkaPropertiesReader;

public class EventUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(EventUtils.class);
	private String envName;
	private String topicName;
	private String schemaName;
	private int partition;
	private String eventKey;
	private String propUrl;
    private RecordHeaders headers;

	/**
	 * Constructor with set the information related to the topic and schema for
	 * produce to event
	 * 
	 * @param file
	 *            The name of the configuration file which contains the environment property
	 * @param property
	 *            The name of the environment property
	 * @param topicName
	 *            The name of the topi
	 **/
	public EventUtils(String file,String property,String topicName) throws Exception {
		this.setEnvName(property, file);
		this.setPropUrl();
		this.setTopicName(topicName);
		LOGGER.info("TOPIC: " + getTopicName());
		LOGGER.info("URLProperty: " + getPropUrl());
	}

	/**
	 * Constructor with set the information related to the topic and schema for
	 * produce and consume to event
	 * 
	 * @param file
	 *           The name of the configuration file which contains  where the property of the environment
	 * @param property
	 *           The property of the environment
	 * @param topicName
	 *            The name of the topic
	 * @param schemaName
	 *           The schema of the topic
	 * @param partition
	 *           The number of the kakfa partition
	 * @param eventKey
	 *            The event key of the event  
	 * @param destination
	 *            The destination of the event header
	 * @param name
	 *           The name of the event header  
	 * @param user
	 *          The user name of the test launch environment                     
	 **/
	public EventUtils(String file,String property,String topicName, String schemaName, int partition, String eventKey,String destination, String name, String user) {
		this.setEnvName(property, file);
		this.setPropUrl();
		this.setTopicName(topicName);
		this.setSchemaName(schemaName);
		this.setPartition(partition);
		this.setEventKey(eventKey);
		this.setHeaders(destination, name, user);
		LOGGER.info("URLProperty: " + getPropUrl());
		LOGGER.info("TOPIC: " + getTopicName());
		LOGGER.info("SCHEMA: " + getSchemaName());
		LOGGER.info("PARTITION: " + getPartition());
		LOGGER.info("EVENTKEY: " + getEventKey());
        LOGGER.info("HEADER"+getHeaders());
		
	

	}
	
	public RecordHeaders  getHeaders() {
		return headers;
	}
	public  void setHeaders(RecordHeaders headers) {
		this.headers = headers;
	}
	
	 /**
     * method for create a headers for the event
     * @param destination capability event
     * @param name name of the owner team
     * @param user name of the test launch environment 
     * @return
     * the related created headers
     */
	public  RecordHeaders setHeaders(String destination, String name, String user) {
		Map<String, String> headerMap = new HashMap<>();
 	   headerMap.put("dynamic.event.destination", destination);
        headerMap.put("ef.name", name);
        headerMap.put("ef.user", user);  
        headers=AvroProducerRecordUtils.createRecordHeaders(headerMap);
        return headers;
	}
	 
	/**
	 * @return the topicName
	 */
	public String getTopicName() {
		return topicName;
	}

	/**
	 * @param topicName
	 *            the topicName to set
	 */
	public void setTopicName(String topicName) {
		this.topicName = getEnvName() + "-" + topicName;
	}

	/**
	 * @return the schemaName
	 */
	public String getSchemaName() {
		return schemaName;
	}

	/**
	 * @param schemaName
	 *            the schemaName to set
	 */
	public void setSchemaName(String schemaName) {
		String suffix = "-value";
		this.schemaName = schemaName + suffix;
	}

	/**
	 * @return the partition
	 */
	public int getPartition() {
		return partition;
	}

	/**
	 * @param partition
	 *            the partition to set
	 */
	public void setPartition(int partition) {
		this.partition = partition;
	}

	/**
	 * @return the eventKey
	 */
	public String getEventKey() {
		return eventKey;
	}

	/**
	 * @param eventKey
	 *            the eventKey to set
	 */
	public void setEventKey(String eventKey) {
		this.eventKey = eventKey;
	}

	/**
	 * @return the consumer's address
	 */
	public String getPropUrl() {
		return propUrl;
	}

	/**
	 * initialize the consumer's address
	 */
	public void setPropUrl() {
		this.propUrl = "conf/" + getEnvName() + "/default-kafka.properties";
	}

	/**
	 * @return the the name of the environment
	 */
	public String getEnvName() {
		return envName;
	}

	/**
	 * @param the
	 *            name of the configuration file
	 * 
	 * @param the
	 *            name of the property file file
	 * 
	 */
	public void setEnvName(String property, String file) {
		this.envName = KafkaPropertiesReader.read().get(file).getProperty(property);
	}

	@Override
	public String toString() {
		return "Event{" + ", topicName='" + topicName + '\'' + ", schemaName='" + schemaName + '\'' + ", partition='"
				+ partition + '\'' + ", eventKey='" + eventKey + '\'' + ", propUrl='" + propUrl + '\'' + '}';
	}
	
}
