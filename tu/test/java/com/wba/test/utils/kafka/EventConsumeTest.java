/*
 * Copyright 2019 Walgreen Co.
 */

package com.wba.test.utils.kafka;

import com.wba.test.utils.BaseStep;
import com.wba.test.utils.PropertiesReader;
import org.testng.annotations.BeforeClass;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class EventConsumeTest extends BaseStep {

    @BeforeClass
    public void createData() {
        dataStorage.add("uniqueID", UUID.randomUUID().toString());
        dataStorage.add("~~startTime", asMillSec("timestamp::"));
        String fileName = "conf/system.properties";
        try {
            PropertiesReader.read("conf/system").forEach((k, v) ->  System.setProperty("" + k, "" + v));
            LOGGER.trace("set " + fileName);
        } catch (RuntimeException e) {
            LOGGER.error("error reading " + fileName);
        }
    }


//    @Test
    public void tConsumeEventWithKey() {
        Map<String, Object> filter = new HashMap<>();
        final List<Event> event = consumeEvents("event", "dev-rxfinalvalidation_complete", filter, 0, false);
//        final List<Event> event = consumeEvents("event", "dev-rxplan_createupdate", filter, 0, false);
    }

//    @Test
//    public void tConsumeEventString() {
//        Map<String, Object> filter = new HashMap<>();
//        filter.put("eventProducedTime", "1549540614476");
//
//        final List<Event> event = consumeEvents("event", "~devqe-notification_pharmacy_12345", filter, 0, false);
//    }


}

