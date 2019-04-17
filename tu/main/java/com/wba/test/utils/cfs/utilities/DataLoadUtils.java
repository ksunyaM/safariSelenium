/* Copyright 2018 Walgreen Co.*/
package com.wba.test.utils.cfs.utilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DataLoadUtils {
    
    private static DataLoadUtils instance;
    private static final Logger LOGGER = LoggerFactory.getLogger(DataLoadUtils.class);
    
    private DataLoadUtils()
    {
    }
    /**
     * method for create and get a instance of DataLoadUtils class
     * @return
     * the related created instance
     */
    public static DataLoadUtils getInstance()
    {
        if (instance == null)
        {
            instance = new DataLoadUtils();
        }
        return instance; 
    }
    
    

}
