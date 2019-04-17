package com.wba.rxdata.test;

import com.wba.test.utils.BaseStep;
import cucumber.api.DataTable;
import org.slf4j.Logger;

import static com.oneleo.test.automation.core.LogUtils.log;

public class _BaseStep extends BaseStep {

    protected static final String RESOURCES_FOLDER_PATH = "com/wba/rxdata/test/data/";

    static final Logger LOGGER = log(_BaseStep.class);

    protected String prepareText(String text, DataTable dataTable) {
        Object[] objects = dataTable.asLists(String.class).stream()
                .map(s -> defineValue(s.get(0)))
                .toArray();
        return String.format(text, objects);
    }
}

