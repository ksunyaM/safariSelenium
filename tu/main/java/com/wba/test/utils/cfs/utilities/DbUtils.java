/* Copyright 2018 Walgreen Co.*/
package com.wba.test.utils.cfs.utilities;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.datastax.driver.core.Row;

public class DbUtils {
    
    private static DbUtils instance;
    private static final Logger LOGGER = LoggerFactory.getLogger(DbUtils.class);

    private DbUtils()
    {
    }
    /**
     * method for create and get a instance of RestUtils class
     * @return
     * the related created instance
     */
    public static DbUtils getInstance()
    {
        if (instance == null)
        {
            instance = new DbUtils();
        }
        return instance; 
    }
    
    /**
     * method that calculate the number of record in the DB
     * @param listRow it's the list of record by query on DB
     * @return the number of row of my query
     * 
     */
    public static int numbRowDB(List<Row> listRow) {
        return listRow.size();
    }

}
