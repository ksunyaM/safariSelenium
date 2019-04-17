/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils.dbSnapshot;

import com.datastax.driver.core.Session;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.wba.test.utils.*;
import com.wba.test.utils.kafka.EventBuilder;
import com.wba.test.utils.kafka.EventStorage;
import cucumber.runtime.CucumberException;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;

import java.util.*;

import static com.oneleo.test.automation.core.DatabaseUtils.cassandra;

public class SnapshotUtils extends BaseStep {

    private String resourceName = "default";

    public SnapshotUtils() {
    }

    public SnapshotUtils setResourceName(String resourceName) {
        this.resourceName = resourceName;
        return this;
    }

    public void updateSnapshot(Map<String, String> data, String... snapshotName) {
        final String eventName = Utils.defaultOrFirst(eventStorage.getLastProduced().getName(), snapshotName);
        String json = eventStorage.findEvent(eventName).getBody();
        for (Map.Entry<String, String> entry : data.entrySet()) {
            json = updateSnapshot(json, entry.getKey(), entry.getValue());
        }
        eventStorage.findEvent(eventName).setBody(json);
    }

    /**
     * 'Smart' snapshot update.
     * If key contains array element -> update exactly by key (expecting that key contains table name), else update all tables were key exist
     * key/value will be added to data storage (old values will be rewrote)
     *
     * @param json  -snapshot
     * @param key   - path to element
     * @param value - new value
     * @return updated snapshot
     */
    public String updateSnapshot(String json, String key, String value) {
        // if key contains array element -> update exactly by key (expecting that key contains table name)
        // else update all tables were key exist
        Object realValue = defineValue(value);
        dataStorage.map().put("~" + key, realValue);

        if (key.contains("[")) {
            if (!JsonUtils.isJsonPathExists(json, key)) {
                LOGGER.trace("Key will be added: " + key + " / " + value);
            }
            return JsonUtils.setJsonAttribute(json, key, value);
        } else {
            String res = json;
            for (Iterator<Map.Entry<String, JsonNode>> i = ((ObjectNode) JsonUtils.getJsonValue(json, "$")).fields(); i.hasNext(); ) {
                final String table = i.next().getKey();
                if (JsonUtils.isJsonPathExists(res, table + "[0]")) {
                    int c = 0;
                    while (JsonUtils.isJsonPathExists(res, table + "[" + c + "]")) {
                        res = updateJsonIfPathExist(res, table + "[" + c + "]." + key, realValue);
                        c++;
                    }
                } else {
                    res = updateJsonIfPathExist(res, table + "." + key, realValue);
                }
            }
            return res;
        }
    }

    private String updateJsonIfPathExist(String json, String key, Object value) {
        return JsonUtils.prettify(JsonUtils.isJsonPathExists(json, key) ? JsonUtils.setJsonAttribute(json, key, value) : json);
    }

    public String getJSONsFromCassandra(Integer countExpected, String query, boolean severalItems, Comparator<Object> comparator, Object... params) {
        final List<Map<String, Object>> maps = new DataBaseUtils(resourceName).runCassandraQuery(query, params);
        String res;
        if (countExpected != null) {
            Assert.assertEquals("expecting " + countExpected + " row(s) for query " + query,
                    Long.parseLong("" + countExpected), maps.size());
        }
        if (maps.size() == 0) {
            return severalItems ? "[]" : "{}";
        }
        if (!severalItems) {
            res = maps.get(0).get("[json]").toString();
        } else {
            List<String> r = new ArrayList<>();
            for (Map<String, Object> map : maps) {
                r.add(map.get("[json]").toString());
            }
            if (comparator != null) {
                r.sort(comparator);
            }
            res = "[" + String.join(",", r) + "]";
        }
        return JsonUtils.prettify(res);
    }

    public void insertSnapshot(String json) {
        for (Iterator<Map.Entry<String, JsonNode>> i = ((ObjectNode) JsonUtils.getJsonValue(json, "$")).fields(); i.hasNext(); ) {
            final Map.Entry<String, JsonNode> entry = i.next();
            final String table = entry.getKey();
            if (((ArrayNode) JsonUtils.getJsonValue(json, table + "..*")).size() == 0) {
                LOGGER.debug("Nothing to insert, table is empty: " + table);
            } else if (JsonUtils.isJsonPathExists(json, table + "[0]")) {
                int c = 0;
                while (JsonUtils.isJsonPathExists(json, table + "[" + c + "]")) {
                    new DataBaseUtils(resourceName).insertJsonToCassandra(table, JsonUtils.getJsonValue(json, table + "[" + c + "]").toString());
                    c++;
                }
            } else {
                new DataBaseUtils(resourceName).insertJsonToCassandra(table, JsonUtils.getJsonValue(json, table).toString());
            }
        }
    }

    public int createSnapshotAndDiff(SnapShotBuilder builder) {
        String name = builder.getName();
        final int num = eventStorage.getNextEventNumber(name);
        final String snapshotName = name + num;

        // create snapShot
        final String json = builder.build();
        eventStorage.addProduced(new EventBuilder().body(json).name(snapshotName).build());
        LOGGER.info("SnapShot created: " + snapshotName);

//        Create diff
//        if (num > 0) {
//            final String diffName = String.format("diff_%s_%d_%d", name, (num - 1), num);
//            final String diff = JsonUtils.compareJSONs(
//                    eventStorage.findEvent(name + (num - 1)).getBody(),
//                    eventStorage.findEvent(name + num).getBody());
//            dataStorage.add(diffName, diff);
////            LOGGER.debug("Diff created: " + diffName);
//        }
        return num;
    }

    /**
     * Return List of fields/values if field has different values in tables, otherwise return null
     */
    public List<List<Object>> verifyDataConsistency(String field, String json, List<String> tables, Long... timeDiff) {
        final Long timeDelta = Utils.defaultOrFirst(1000L, timeDiff);

        List<List<Object>> values = new ArrayList<>();
        tables.stream()
                .map(t -> t + (JsonUtils.isJsonPathExists(json, t + "[0]") ? "[0]" : "") + "." + field)
                .filter(p -> JsonUtils.isJsonPathExists(json, p))
                .forEach(p -> values.add(Arrays.asList(p, VerificationUtils.convert(JsonUtils.getJsonValue(json, p)))));

        boolean isEqual = true;
        final Object firstElement = values.get(0).get(1);
        for (List<Object> value : values) {
            // datetime values maybe differ because when tables was updated current time was used for each tables
            final Object currentElement = value.get(1);
            if (currentElement != null &&
                    currentElement.toString().matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{3}Z")) {
                isEqual = Math.abs(asMillSec(firstElement.toString()) - asMillSec(currentElement.toString())) <= timeDelta;
            } else {
                try {
                    Assert.assertEquals(values.toString(), currentElement, firstElement);
                } catch (AssertionError e) {
                    isEqual = false;
                }
            }
        }
        if (!isEqual) LOGGER.warn(values.toString());
        return isEqual ? null : values;
    }

    /**
     * Prepare multimap were key is fields and tables were this field is mentioned is a value
     *
     * @param tables - list
     * @return - a multimap
     */
    public Multimap<String, String> createFieldList(List<String> tables) {
        Multimap<String, String> res = HashMultimap.create();
        tables.forEach(t -> res.putAll(getFields(t)));
        return res;
    }

    /**
     * Parse the cassandra table and obtain the first level fields. MultiMap is returned for compatibility with analyzeTable
     *
     * @param table - cassandra table name
     * @return multi map: key field, value table
     */
    public Multimap<String, String> getFields(String table) {
        Multimap<String, String> res = HashMultimap.create();

        final Session session = cassandra().template();
        final String desc = session.getCluster().getMetadata().getKeyspace(session.getLoggedKeyspace()).getTable(table).asCQLQuery();

        final String[] strings = RegExp.getMatches(desc, "\\((.+)PRIMARY KEY").get(0);
        final String s = RegExp.replaceAll(strings[0], " map<[^>]+>", "");
        RegExp.getMatches(s, "([^ ]+) ([^ ]+),").forEach(a -> res.put(a[0], table));
        return res;
    }

    /**
     * transform createFieldList multimap to xls view
     *
     * @param tables - list
     * @return - a string that should be copied in Excel
     */
    public String analyzeTables(List<String> tables) {
        final Multimap<String, String> fieldList = createFieldList(tables);
        StringBuilder matrix = new StringBuilder();
        fieldList.keySet().stream().sorted().forEach(field -> {
            matrix.append(field).append("\t");
            tables.forEach(t -> matrix.append(fieldList.get(field).contains(t) ? t : "").append("\t"));
            matrix.append("\n");
        });
        return matrix.toString();
    }

    public String getNextEventName(String... name) {
        String n = Utils.defaultOrFirst(SnapShotBuilder.DEFAULT_SNAPSHOT_NAME, name);
        return n + eventStorage.getNextEventNumber(n);
    }

    /**
     * This is alias for the same method from eventStorage. @link
     *
     * @param snapshotName - name of event (w/o number)
     * @return the name of last snapshot or null
     */
    public String getLastSnapshotName(String... snapshotName) {
        return EventStorage.getInstance().getLastSnapshotName(snapshotName);
    }


    // ********** Actions with snapshot ********** //

    public Integer getRowsInTable(String tableName, String... snapshotName) {
        return asInt(getJsonName(snapshotName) + "." + tableName + ".length()");
    }

    private String getJsonName(String[] snapshotName) {
        return Utils.defaultOrFirst(eventStorage.getLastSnapshotName(), snapshotName);
    }

    /**
     * Returns path to the first row in a specified table in a snapshot that has specified field and value.
     * Used for search element (row) in snapshot where one of a field has a certain value
     *
     * @param tableName    name of table in snapshot
     * @param fieldName    path to field
     * @param value        string representation of value
     * @param snapshotName optional.
     * @return path to element in table
     */
    public String getRowByValue(String tableName, String fieldName, String value, String... snapshotName) {
        final String _snapshotName = getJsonName(snapshotName);
        for (int i = 0; i < getRowsInTable(tableName, snapshotName); i++) {
            final String row = _snapshotName + "." + tableName + "[" + i + "].";
            final String _value = asStr(row + fieldName);
            if (StringUtils.equals(_value, value)) return row;
        }
        throw new CucumberException(String.format("row is not found in table %s for field %s with value %s", tableName, fieldName, value));
    }

    /**
     * Return all values for specified field from each row in specified table.
     *
     * @return list of values
     */
    public List<String> getRowsValue(String tableName, String fieldName, String... snapshotName) {
        List<String> res = new ArrayList<>();
        final String _snapshotName = getJsonName(snapshotName);
        for (int i = 0; i < getRowsInTable(tableName, snapshotName); i++) {
            final String row = _snapshotName + "." + tableName + "[" + i + "].";
            res.add(asStr(row + fieldName));
        }
        return res;
    }

}
