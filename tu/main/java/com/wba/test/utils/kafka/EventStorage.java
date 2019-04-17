/* Copyright 2018 Walgreen Co. */
package com.wba.test.utils.kafka;

import com.wba.test.utils.Utils;
import com.wba.test.utils.dbSnapshot.SnapShotBuilder;

import java.util.*;

public class EventStorage {

    private static EventStorage instance;

    private List<Event> producedEvents = new ArrayList<>();
    private LinkedHashMap<String, LinkedList<Event>> consumedEvents = new LinkedHashMap<>(); // grouped by topic name

    private String lastTopicName;
    private String lastEventNameProduced;
    private String lastEventNameConsumed;

    private EventStorage() {
        instance = this;
    }

    public static EventStorage getInstance() {
        return instance == null ? new EventStorage() : instance;
    }

    public static void reset() {
        instance = new EventStorage();
    }

    public String addProduced(Event event) {
        producedEvents.add(event);
        setLastEventNameProduced(event.getName());
        setLastTopicName(event.getTopicName());
        return event.getName();
    }

    public String addConsumed(Event event) {
        EventUtils.doEventAction(event, Event.Action.DECRYPT_FIELDS);
        List<Event> events = consumedEvents.computeIfAbsent(event.getTopicName(), k -> new LinkedList<>());
        events.add(event);

        setLastEventNameConsumed(event.getName());
        setLastTopicName(event.getTopicName());

        return event.getName();
    }

    public Event getLastProduced() {
        return getProduced(getLastEventNameProduced());
    }

    public Event getLastConsumed() {
        return getConsumed(getLastEventNameConsumed());
    }

    public Event getProduced(String eventName) {
        return producedEvents.stream()
                .filter(event -> event.getName().equalsIgnoreCase(eventName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Produced event is not found by name: " + eventName));
    }

    public Event getConsumed(String eventName) {
        return consumedEvents.values().stream()
                .flatMap(Collection::stream)
                .filter(e -> e.getName().equals(eventName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Consumed event is not found by name: " + eventName));
    }

    /**
     * Will find event in all storages (e.g produced & consumed)
     * Aliases:
     * "P" - last produced event
     * "C" - last consumed event
     * Throw error if event not found or more than one event is found
     *
     * @param eventName
     * @return
     */
    public Event findEvent(String eventName) {
        String name = eventName.equalsIgnoreCase("p") ? getLastEventNameProduced() :
                eventName.equalsIgnoreCase("c") ? getLastEventNameConsumed() : eventName;

        Event produced = null;
        Event consumed = null;

        try {
            produced = getProduced(name);
        } catch (RuntimeException ignored) {
        }
        try {
            consumed = getConsumed(name);
        } catch (RuntimeException ignored) {
        }

        if (consumed != null && produced != null) {
            throw new RuntimeException("Found produced and consumed events with the same name: " + eventName);
        } else if (consumed == null && produced == null) {
            throw new RuntimeException("Event not found: " + eventName);
        }

        return produced != null ? produced : consumed;
    }

    public int getNextEventNumber(String name) {
        int num = 0;
        while (true) {
            try {
                findEvent(name + num);
                num++;
            } catch (RuntimeException e) {
                return num;
            }
        }
    }

    /**
     * If snapshot name is missing returns the name of the last snapshot. If snapshot is not exist in event storage null will be returned.
     *
     * @param snapshotName event/snapshot name (optional)
     * @return a snapshot/event with biggest number e.g. Snapshot2
     */
    public String getLastSnapshotName(String... snapshotName) {
        final String name = Utils.defaultOrFirst(SnapShotBuilder.DEFAULT_SNAPSHOT_NAME, snapshotName);
        final int nextEventNumber = getNextEventNumber(name);
        return nextEventNumber == 0 ? null : name + (nextEventNumber - 1);
    }


    // -------------- Generated ---------------

    public String getLastTopicName() {
        return lastTopicName;
    }

    public void setLastTopicName(String lastTopicName) {
        this.lastTopicName = lastTopicName;
    }

    public String getLastEventNameProduced() {
        return lastEventNameProduced;
    }

    public void setLastEventNameProduced(String lastEventNameProduced) {
        this.lastEventNameProduced = lastEventNameProduced;
    }

    public String getLastEventNameConsumed() {
        return lastEventNameConsumed;
    }

    public void setLastEventNameConsumed(String lastEventNameConsumed) {
        this.lastEventNameConsumed = lastEventNameConsumed;
    }
}
