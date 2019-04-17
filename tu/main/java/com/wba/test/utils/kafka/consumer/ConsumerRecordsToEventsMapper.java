/*
 * Copyright 2018 Walgreen Co.
 */
package com.wba.test.utils.kafka.consumer;

import com.wba.test.utils.kafka.Event;
import com.wba.test.utils.kafka.EventBuilder;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

public class ConsumerRecordsToEventsMapper {

    public static List<Event> map(List<ConsumerRecord<Object, Object>> consumerRecords) {
        List<Event> events = new ArrayList<>(consumerRecords.size());
        consumerRecords.forEach(record -> {
            Map<String, String> headers = Stream.of(record.headers().toArray())
                    .collect(toMap(Header::key, h -> new String(h.value() != null ? h.value() : new byte[0])));
            final EventBuilder e = Event.builder()
                    .topicName(record.topic())
                    .partition(record.partition())
                    .body(record.value().toString())
                    .bodyKey(Optional.ofNullable(record.key()).map(Object::toString).orElse(null))
                    .headers(headers);
            if (record.value() != null && record.value() instanceof GenericRecord)
                e.schemaName(((GenericRecord) record.value()).getSchema().getName());
            if (record.key() != null && record.key() instanceof GenericRecord)
                e.schemaNameKey(((GenericRecord) record.key()).getSchema().getName());
            events.add(e.build());
        });
        return events;
    }
}
