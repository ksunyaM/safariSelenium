/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/

package com.wba.test.utils.commonsteps;

import com.wba.test.utils.ResourceUtils;
import cucumber.api.DataTable;

public class CommonStepSimple extends BaseStep {

    public CommonStepSimple setResourceFolderPath(String resourceFolderPath){
        this.resourcesFolderPath = resourceFolderPath;
        return this;
    }

    public void eventForTopic(String topicName) {
        new CommonStep().eventForTopic(null, topicName);
    }

    public void schemaNameIs(String schemaName) {
        new CommonStep().verifySchemaExists(schemaName);
        eventStorage.getLastProduced().setSchemaName(schemaName);
    }

    public void updateJson(DataTable data) {
        updateJson(eventStorage.getLastProduced(), data);
    }

    public void eventHasJson(String jsonFileName) {
        eventStorage.getLastProduced().setBody(
                ResourceUtils.getResourceAsString(resourcesFolderPath + jsonFileName));
    }

    public void eventHasJsonFromEvent(String eventName) {
        eventStorage.getLastProduced().setBody(eventStorage.findEvent(eventName).getBody());
    }

    public void eventIsProducedByTest() {
        new CommonStep().eventIsProducedByTest(eventStorage.getLastEventNameProduced());
    }

    public void newEventIsProducedToTopic(String topicName) {
        new CommonStep().newEventIsProducedToTopic(null, topicName);
    }

    public void newEventIsProduced() {
        new CommonStep().newEventIsProducedToTopic(null, eventStorage.getLastTopicName());
    }

    public void newEventIsProducedToTopicWithData(String topicName, DataTable data) {
        new CommonStep().newEventIsProducedToTopicWithData(null, topicName, data);
    }

    public void newEventIsProducedToTopicWithDataFromEvent(String topicName, DataTable data) {
        new CommonStep().newEventIsProducedToTopicWithDataFromEvent(null, topicName, eventStorage.getLastEventNameProduced(), data);
    }
}
