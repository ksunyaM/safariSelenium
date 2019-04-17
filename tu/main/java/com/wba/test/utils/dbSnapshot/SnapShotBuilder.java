/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils.dbSnapshot;

import com.wba.test.utils.JsonUtils;
import com.wba.test.utils.RegExp;
import com.wba.test.utils.Utils;
import cucumber.runtime.CucumberException;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SnapShotBuilder {

    public final static String DEFAULT_SNAPSHOT_NAME = "Snapshot";

    private String name = DEFAULT_SNAPSHOT_NAME;

    private DbType currentDbType = DbType.CASSANDRA;

    private String resourceName = "default";

    private List<SnapShotItem> items = new ArrayList<>();

    public String build() {
        List<String> resList = new ArrayList<>();

        for (SnapShotItem item : items) {
            if (item.getDbType() == DbType.CASSANDRA && item.getQuery().toLowerCase().contains(" json ")) {
                resList.add(String.format("\"%s\": %s",
                        getTableNameFromQuery(item.getQuery()),
                        new SnapshotUtils()
                                .setResourceName(resourceName)
                                .getJSONsFromCassandra(
                                        item.getRecordExpected(),
                                        item.getQuery(),
                                        item.isSeveralItems(),
                                        item.getComparator(),
                                        item.getParams())
                ));
            } else {
                throw new UnsupportedOperationException("not supported yet:" + item.toString());
            }
        }
        return JsonUtils.prettify(String.format("{%s}", String.join(",", resList)));
    }

    private SnapShotItem getLastItem() {
        if (items.size() > 0) return items.get(items.size() - 1);
        else throw new CucumberException("no items found");
    }

    public String getName() {
        return StringUtils.defaultIfEmpty(name, DEFAULT_SNAPSHOT_NAME);
    }

    public SnapShotBuilder name(String snapShotName) {
        name = snapShotName;
        return this;
    }

    public SnapShotBuilder resourceName(String resourceName) {
        this.resourceName = resourceName;
        return this;
    }

    public SnapShotBuilder forCassandra() {
        currentDbType = DbType.CASSANDRA;
        return this;
    }

    public SnapShotBuilder asText() {
        currentDbType = DbType.TEXT;
        return this;
    }

    public SnapShotBuilder addItem(String query, Object... params) {
        items.add(new SnapShotItem(currentDbType, null, query, false, null, params));
        return this;
    }

    public SnapShotBuilder addItems(String query, Object... params) {
        items.add(new SnapShotItem(currentDbType, null, query, true, null, params));
        return this;
    }

    public SnapShotBuilder expectingOneRow() {
        getLastItem().setRecordExpected(1);
        return this;
    }

    public SnapShotBuilder expectingRows(Integer rowCount) {
        getLastItem().setRecordExpected(rowCount);
        return this;
    }

    public SnapShotBuilder sortingBy(String jsonPath, SortOrder... sortOrder) {

        final Comparator<Object> comparing = Comparator.comparing(s -> JsonUtils.getJsonValue(s.toString(), jsonPath, String.class));
        getLastItem().setComparator(
                Utils.defaultOrFirst(SortOrder.ASC, sortOrder) == SortOrder.ASC ?
                        comparing :
                        comparing.reversed()
        );
        return this;
    }

    private String getTableNameFromQuery(String query) {
        return RegExp.getMatches(query, "select .*?\\s+from\\s+([^ ]+)").get(0)[0];
    }


    public enum SortOrder {
        ASC, DESC;
    }

    public enum DbType {
        CASSANDRA, ORACLE, TEXT;
    }

    public class SnapShotItem {
        private DbType dbType;
        private Integer recordExpected;
        private String query;
        private Object[] params;
        private boolean severalItems;
        private Comparator<Object> comparator;

        public SnapShotItem() {
        }

        public SnapShotItem(DbType dbType, Integer recordExpected, String query, boolean severalItems, Comparator<Object> comparator, Object... params) {
            this.dbType = dbType;
            this.recordExpected = recordExpected;
            this.query = query;
            this.severalItems = severalItems;
            this.comparator = comparator;
            this.params = params;
        }

        public DbType getDbType() {
            return dbType;
        }

        public void setDbType(DbType dbType) {
            this.dbType = dbType;
        }

        public Integer getRecordExpected() {
            return recordExpected;
        }

        public void setRecordExpected(Integer recordExpected) {
            this.recordExpected = recordExpected;
        }

        public String getQuery() {
            return query;
        }

        public void setQuery(String query) {
            this.query = query;
        }

        public Object[] getParams() {
            return params;
        }

        public void setParams(Object[] params) {
            this.params = params;
        }

        public boolean isSeveralItems() {
            return severalItems;
        }

        public void setSeveralItems(boolean severalItems) {
            this.severalItems = severalItems;
        }

        public Comparator<Object> getComparator() {
            return comparator;
        }

        public void setComparator(Comparator<Object> comparator) {
            this.comparator = comparator;
        }
    }
}
