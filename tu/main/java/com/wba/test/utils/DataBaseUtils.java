/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils;

import com.datastax.driver.core.*;
import cucumber.runtime.CucumberException;
import org.slf4j.Logger;

import java.util.*;

import static com.oneleo.test.automation.core.DatabaseUtils.cassandra;
import static com.oneleo.test.automation.core.DatabaseUtils.jdbc;
import static com.oneleo.test.automation.core.LogUtils.log;

public class DataBaseUtils {
    private static final Logger LOGGER = log(DataBaseUtils.class);

    private String resourceName = "default";

    public DataBaseUtils() {
    }

    public DataBaseUtils(String resourceName) {
        this.resourceName = resourceName;
    }

    public Map<String, Object> runSQLForOneRowConditions(String mainQuery, String tail, String... conditions) {
        String condition = String.join(" AND ", conditions);
        String query = mainQuery + (condition.length() > 0 ? " where " + condition : "");
        return runSQLForOneRowParameters(resourceName, query, tail);
    }

    public Map<String, Object> runSQLForOneRowParameters(String query, String tail, Object... parameters) {
        LOGGER.info(String.format("Going to find by query: %s\n with parameters: %s", query, Arrays.toString(parameters)));
        Map<String, Object> map = jdbc().template(resourceName).queryForMap(query, parameters);
        Map<String, Object> result = new HashMap<>();
        map.forEach((k, v) -> result.put(k + tail, v));
        LOGGER.info(String.format("For query:\n %s; with parameters: %s\n result is:\n %s", query, Arrays.toString(parameters), result));
        return result;
    }

    public List<Map<String, Object>> runSQLForListRowsParameters(String query, Object... parameters) {
        LOGGER.info(String.format("Going to find by query: %s\n with parameters: %s", query, Arrays.toString(parameters)));
        List<Map<String, Object>> list = jdbc().template(resourceName).queryForList(query, parameters);
        LOGGER.info(String.format("For query:\n %s; with parameters: %s\n result is:\n %s", query, Arrays.toString(parameters), list));
        return list;
    }

    public void runSQLQuery(String query) {
        LOGGER.info(String.format("Going to execute query: %s", query));
        jdbc().template(resourceName).execute(query);
    }

    public List<Map<String, Object>> runCassandraQuery(String query, Object... parameters) {
        LOGGER.info(String.format("Going to find by query: %s\n with parameters: %s", query, Arrays.toString(parameters)));
        Session session = cassandra().template(resourceName);
        PreparedStatement prepared = session.prepare(query);
        ResultSet rs = session.execute(prepared.bind(parameters));
        List<Map<String, Object>> resultList = convertResultSetToListOfMaps(rs);
        LOGGER.info(String.format("Query result:\n %s \n", resultList));
        return resultList;
    }

    public void insertJsonToCassandra(String table, String json) {
        String query = "INSERT INTO " + table + " JSON '" + json + "';";
        if (json.contains("::")) {
            throw new CucumberException("found not replaced template" + query);
        }
        new DataBaseUtils(resourceName).runCassandraQuery(query);
    }

    private List<Map<String, Object>> convertResultSetToListOfMaps(ResultSet rs) {
        List<Row> rows = rs.all();
        List<Map<String, Object>> resultList = new ArrayList<>();

        if (!rows.isEmpty()) {
            for (Row row : rows) {
                HashMap<String, Object> resultRow = new HashMap<>();
                for (int i = 0; i < row.getColumnDefinitions().size(); i++) {
                    String columnName = row.getColumnDefinitions().getName(i);
                    DataType columnType = row.getColumnDefinitions().getType(i);
                    resultRow.put(columnName, getDynamicColumn(row, columnName, columnType));
                }
                resultList.add(resultRow);
            }
        }
        return resultList;
    }

    private Object getDynamicColumn(Row row, String columnName, DataType dataType) {
        switch (dataType.getName()) {
            case ASCII:
                return row.getString(columnName);
            case BIGINT:
                return VerificationUtils.toBigDecimal(row.getLong(columnName));
            case SMALLINT:
                return VerificationUtils.toBigDecimal(row.getShort(columnName));
            case BLOB:
                return row.getBytes(columnName);
            case BOOLEAN:
                return row.getBool(columnName);
            case COUNTER:
                return VerificationUtils.toBigDecimal(row.getLong(columnName));
            case DECIMAL:
                return VerificationUtils.toBigDecimal(row.getDecimal(columnName));
            case DOUBLE:
                return VerificationUtils.toBigDecimal(row.getDouble(columnName));
            case FLOAT:
                return VerificationUtils.toBigDecimal(row.getFloat(columnName));
            case INET:
                return row.getInet(columnName);
            case INT:
                return VerificationUtils.toBigDecimal(row.getInt(columnName));
            case TEXT:
                return row.getString(columnName);
            case TIMESTAMP:
                return row.getTimestamp(columnName) == null ? null : row.getTimestamp(columnName).getTime();
            case UUID:
                return row.getUUID(columnName) == null ? null : row.getUUID(columnName).toString();
            case VARCHAR:
                return row.getString(columnName);
            case VARINT:
                return VerificationUtils.toBigDecimal(row.getLong(columnName));
            case TIMEUUID:
                return row.getUUID(columnName);
            case DATE:
                return row.getDate(columnName);
            case TINYINT:
                return VerificationUtils.toBigDecimal(row.getLong(columnName));
            case LIST:
                return row.getObject(columnName);
            case SET:
                return row.getObject(columnName);
            case MAP:
                return row.getObject(columnName);
            case CUSTOM:
                return row.getObject(columnName);
            case UDT:
                return row.getObject(columnName);
            case DURATION:
            case TIME:
            case TUPLE:
            default:
                throw new UnsupportedOperationException("Unrecognized object for column: " + columnName);
        }
    }
}
