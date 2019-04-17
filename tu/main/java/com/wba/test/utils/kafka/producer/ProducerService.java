/*
 * Copyright 2018 Walgreen Co.
 */
package com.wba.test.utils.kafka.producer;

import com.oneleo.test.automation.core.kafka.AvroProducerRecordUtils;
import com.wba.test.utils.kafka.Event;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.slf4j.Logger;
import org.springframework.kafka.support.SendResult;

import static com.oneleo.test.automation.core.LogUtils.log;
import static com.oneleo.test.automation.core.MessageUtils.kafka;

public class ProducerService {

    private static final Logger LOGGER = log(ProducerService.class);

    public void produce(Event event) {
        try {
            RecordHeaders recordHeaders = AvroProducerRecordUtils.createRecordHeaders(event.getHeaders());
            LOGGER.info("Producing an event: {}", event.toString());
            ProducerRecord<Object, Object> record;
            if (event.getSchemaNameKey() != null) {
                record = AvroProducerRecordUtils.createAvroRecordWithHeadersAndSchemaKey(
                        event.getTopicName(),
                        event.getSchemaNameKey(),
                        event.getSchemaName(),
                        event.getBody(),
                        recordHeaders,
                        event.getPartition(),
                        event.getBodyKey()
                );
            } else {
                record = AvroProducerRecordUtils.createAvroRecordWithHeaders(
                        event.getTopicName(),
                        event.getSchemaName(),
                        event.getBody(),
                        recordHeaders,
                        event.getPartition(),
                        null);
            }
            SendResult<Object, Object> sendResult = kafka().template().send(record).get();
            LOGGER.debug("Event was produced to partition {}", sendResult.getRecordMetadata().toString());
        } catch (Exception e) {
            throw new RuntimeException("Unable to produce an event", e);
        }
    }
}
