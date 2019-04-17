/* Copyright 2018 Walgreen Co. */
package com.wba.test.utils.kafka;

import com.wba.test.utils.JsonUtils;
import com.wba.test.utils.ResourceUtils;
import org.testng.annotations.Test;

import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class EventBuilderTest {

    @Test
    public void testBuildEvent() {
        String topicName = "devqe-prescriber";

        Event prescriberEvent = new EventBuilder().applyDefaultsForTopic(topicName).build();

//        assertEquals(topicName, prescriberEvent.getTopicName());
        assertEquals("prescriber-value_qe", prescriberEvent.getSchemaName());
        assertNotNull(prescriberEvent.getName());
        assertEquals(JsonUtils.compareJSONs(
                ResourceUtils.getResourceAsString("com/wba/test/data/defaultPrescriberData.json"),
                prescriberEvent.getBody()),
                "[]");
        Map<String, String> actualHeaders = prescriberEvent.getHeaders();
        assertEquals(4, actualHeaders.size());
        assertEquals("platform", actualHeaders.get("user"));
        assertEquals("CreationPrescriberDataEvent", actualHeaders.get("name"));
        assertEquals("WBA Company", actualHeaders.get("company"));
        assertEquals("inputKafkaCreatePrescriberTO", actualHeaders.get("dynamic.event.destination"));
    }

    @Test
    public void bodyPrettyPrintTest() {
        assertEquals(new EventBuilder().body("q").build().getBody(), "q");
    }
}
