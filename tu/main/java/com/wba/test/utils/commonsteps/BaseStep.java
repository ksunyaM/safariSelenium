/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/

package com.wba.test.utils.commonsteps;

import cucumber.api.DataTable;
import org.slf4j.Logger;

import static com.oneleo.test.automation.core.LogUtils.log;

public class BaseStep extends com.wba.test.utils.BaseStep {

    static final Logger LOGGER = log(BaseStep.class);
    protected String resourcesFolderPath = "undefined";

    protected String prepareText(String text, DataTable dataTable) {
        Object[] objects = dataTable.asLists(String.class).stream()
                .map(s -> defineValue(s.get(0)))
                .toArray();
        return String.format(text, objects);
    }
}
