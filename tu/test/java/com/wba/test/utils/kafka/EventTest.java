/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils.kafka;

import org.apache.commons.lang3.RandomUtils;
import org.testng.annotations.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

/* Copyright 2018 Walgreen Co. */
public class EventTest {

    @Test
    public void testReplaceStringAttributeInEvent() {
        Event event = Event.builder().applyDefaultsForTopic("prescriber").build();
        String corrId = UUID.randomUUID().toString();
        event.replaceBodyAttribute("correlationId", corrId);
        assertEquals(corrId, event.getBodyAttribute("correlationId", String.class));
    }

    @Test
    public void testReplaceNulberAttributeInEvent() {
        Event event = Event.builder().applyDefaultsForTopic("prescriber").build();
        int expirationDate = RandomUtils.nextInt(100000000, 900000000);
        event.replaceBodyAttribute("licenses.array[0].expirationDate.int", expirationDate);
        assertEquals(expirationDate, (int) event.getBodyAttribute("licenses.array[0].expirationDate.int", Integer.class));
    }

}
