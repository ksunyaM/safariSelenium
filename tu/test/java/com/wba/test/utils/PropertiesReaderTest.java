/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils;

import org.testng.Assert;

public class PropertiesReaderTest {

    //    @Test
    public void getEnvConfiguration() {
        Assert.assertEquals(
                PropertiesReader.read("conf\\local\\custom-api")
                        .getProperty("apiserver.uri"),
                "http://jsonplaceholder.typicode.com"
        );
    }
}
