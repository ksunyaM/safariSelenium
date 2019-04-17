/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils.kafka;

import org.junit.Assert;
import org.testng.annotations.Test;

import java.util.stream.IntStream;

/* Copyright 2018 Walgreen Co. */
public class EventStorageTest {

    private EventStorage eventStorage = EventStorage.getInstance();

    @Test
    public void eventStorageTest() {

        String producedEventName = "produced";
        String consumedEventName = "consumed";

        Event produced = new EventBuilder().name(producedEventName).build();
        Event consumed = new EventBuilder().name(consumedEventName).build();
        eventStorage.addProduced(produced);
        eventStorage.addConsumed(consumed);

        Assert.assertEquals(eventStorage.getLastEventNameProduced(), producedEventName);
        Assert.assertEquals(eventStorage.getLastEventNameConsumed(), consumedEventName);

        Assert.assertEquals(eventStorage.getLastProduced().getName(), producedEventName);
        Assert.assertEquals(eventStorage.getLastConsumed().getName(), consumedEventName);

        Assert.assertEquals(producedEventName, eventStorage.findEvent("P").getName());
        Assert.assertEquals(producedEventName, eventStorage.findEvent("P").getName());
        Assert.assertEquals(consumedEventName, eventStorage.findEvent("c").getName());
        Assert.assertEquals(consumedEventName, eventStorage.findEvent("C").getName());
    }

    @Test
    public void eventStorageTest01() {
        IntStream.range(1, 10).forEach(i ->
                eventStorage.addProduced(new EventBuilder().name("n" + i).topicName("pt" + i).build()));
        IntStream.range(1, 5).forEach(i ->
                eventStorage.addConsumed(new EventBuilder().name("n" + i).topicName("ct" + i).build()));

        Assert.assertEquals(eventStorage.getProduced("n3").getTopicName(), "pt3");
        Assert.assertEquals(eventStorage.getConsumed("n3").getTopicName(), "ct3");
    }
}
