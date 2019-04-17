/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils;

import org.junit.Test;

import java.util.List;
import java.util.Map;

import static com.wba.test.utils.HiveUtils.hive;

public class HiveUtilsTest extends BaseStep {
    /**
     * Properties files are:
     * hive-database.properties
     * default-kerberos.properties
     *
     * Required files:
     * principal keytab file
     * krb5.ini file
     */

    //@Test
    public void hiveOneLineTest() throws Exception {
        List<Map<String, Object>> res = hive().connect("hive")
                .runSQLForListRowsParameters("SELECT * FROM srxraw.StockTransferDocumentGenerated where correlationid = ? and transfercode = ?", "SIT_Testing", "7600000044");
        LOGGER.info(String.format("Result: %s \n", res));
    }

    //@Test
    public void hiveSeveralQueries() throws Exception{
        HiveUtils hive = hive().connect("hive");
        List<Map<String, Object>> res1 = hive.runSQLForListRowsParameters("SELECT * FROM srxraw.StockTransferDocumentGenerated where correlationid = ?", "SIT_Testing");
        LOGGER.info(String.format("Result: %s \n", res1));

        List<Map<String, Object>> res2 = hive.runSQLForListRowsParameters("SELECT * FROM srxraw.StockTransferDocumentGenerated where transfercode = ?", "7600000044");
        LOGGER.info(String.format("Result: %s \n", res2));
    }
}
