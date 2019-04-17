/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

import static com.oneleo.test.automation.core.LogUtils.log;

public class ZephyrUtils {
    private static final Logger LOGGER = log(ZephyrUtils.class);
    private static final String SYSTEM_PROPERTY_ZEPHYR_TESTCYCLE_NAME = "zephyr.testcycle.name";
    private static final String APU_URL_JIRA = "/jira/rest/api/2/";
    private static final String API_URL_ZEPHYR = "/jira/rest/zapi/latest/";
    private static final String CUCUMBER_TAG_PATTERN_JIRAID = "@jiraid=";
    private static final String PROPERTY_JIRA_BASE_URL = "jira.baseurl";
    private static final String PROPERTY_JIRA_AUTHORIZATION = "jira.authorization";
    private static final String PROPERTY_JIRA_ASSIGNEE = "assignee";
    private static final String PROPERTY_JIRA_RELEASE_VERSION = "release.version";
    private Properties properties = new Properties();
    private String propertiesFileName;
    private String projectKey;
    private String projectId;
    private String releaseVersionId;
    private String testCycleId;
    private String issueId;
    private String executionId;
    private String testCycleName;
    private boolean moveToFolder = false;
    private String folderId;
    private String folderName;

    public ZephyrUtils(String propertiesPrefix) {
        propertiesFileName = propertiesPrefix + "-zephyr.properties";
    }

    public ZephyrUtils() {
        this("default");
    }

    private String performRequest(String requestType, String url, String payload) {
        String responseBody = "";
        Response response;
        Invocation.Builder request = ClientBuilder.newClient()
                .target(url)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Basic " + properties.getProperty(PROPERTY_JIRA_AUTHORIZATION));

        switch (requestType) {
            case "GET":
                response = request.get();
                break;
            case "POST":
                response = request.post(Entity.json(payload));
                break;
            case "PUT":
                response = request.put(Entity.json(payload));
                break;
            default:
                response = null;
                break;
        }

        if (response != null) {
            int status = response.getStatus();
            if (status == 200) {
                responseBody = response.readEntity(String.class);
            } else {
                LOGGER.warn("Status code " + Integer.toString(status) + " occurred, while performing " + requestType +
                        " request with url '" + url + "' and authorization key: '" +
                        properties.getProperty(PROPERTY_JIRA_AUTHORIZATION) + "'");
            }
        } else {
            LOGGER.warn("Unable to perform " + requestType + " request with url " + url);
        }

        return responseBody;
    }

    private String performGetRequest(String url) {
        return performRequest("GET", url, null);
    }

    private String performPostRequest(String url, String payload) {
        return performRequest("POST", url, payload);
    }

    private String performPutRequest(String url, String payload) {
        return performRequest("PUT", url, payload);
    }

    private String getProjectId() {
        String projectId = "";
        String responseString = performGetRequest(properties.getProperty(PROPERTY_JIRA_BASE_URL) +
                APU_URL_JIRA + "project/" + projectKey);

        if (!responseString.isEmpty()) {
            projectId = JsonUtils.getJsonValue(responseString, "id", String.class);
            if (projectId.isEmpty()) {
                LOGGER.warn("Unable to get Project Id");
            }
        } else {
            LOGGER.warn("Get Project Id skipped");
        }

        return projectId;
    }

    private String getReleaseVersionId() {
        String releaseVersionId = "";
        String responseString = performGetRequest(properties.getProperty(PROPERTY_JIRA_BASE_URL) +
                APU_URL_JIRA + "project/" + projectKey + "/versions");

        if (!responseString.isEmpty()) {
            JSONArray arr = new JSONArray(responseString);
            for (Object object : arr) {
                JSONObject obj = (JSONObject) object;
                if (properties.getProperty(PROPERTY_JIRA_RELEASE_VERSION).equals(obj.getString("name"))) {
                    releaseVersionId = obj.getString("id");
                    break;
                }
            }
            if (releaseVersionId.isEmpty()) {
                LOGGER.warn("Unable to get Release Version Id");
            }
        } else {
            LOGGER.warn("Get Release Version Id skipped");
        }

        return releaseVersionId;
    }

    private String getTestCycleId() {
        String testCycleId = "";
        String responseString = performGetRequest(properties.getProperty(PROPERTY_JIRA_BASE_URL) + API_URL_ZEPHYR + "cycle/?projectId=" +
                projectId + "&versionId=" + releaseVersionId);

        if (!responseString.isEmpty()) {
            JSONObject obj = new JSONObject(responseString);
            for (Object key : obj.keySet()) {
                if (!key.equals("recordsCount")) {
                    if (testCycleName.equals(obj.getJSONObject((String) key).getString("name"))) {
                        testCycleId = (String) key;
                        break;
                    }
                }
            }
            if (testCycleId.isEmpty()) {
                LOGGER.warn("Unable to find test cycle with name " + testCycleName);
            }
        } else {
            LOGGER.warn("Get Test Cycle Id skipped");
        }

        return testCycleId;
    }

    private String createTestCycle() {
        String testCycleId = "";
        String payload = "{\"clonedCycleId\": \"\"," +
                "\"name\": \"" + testCycleName + "\"," +
                "\"build\": \"\"," +
                "\"environment\": \"\"," +
                "\"description\": \"Test Cycle created by Automation Framework on " + new Date() + "\"," +
                "\"startDate\": \"\"," +
                "\"endDate\": \"\"," +
                "\"projectId\": \"" + projectId + "\"," +
                "\"versionId\": \"" + releaseVersionId + "\"}";
        String responseString = performPostRequest(properties.getProperty(PROPERTY_JIRA_BASE_URL) + API_URL_ZEPHYR + "cycle", payload);

        if (!responseString.isEmpty()) {
            testCycleId = JsonUtils.getJsonValue(responseString, "id", String.class);
            if (testCycleId.isEmpty()) {
                LOGGER.warn("Unable to create Test Cycle with name " + testCycleName);
            }
        } else {
            LOGGER.warn("Create Test Cycle skipped");
        }

        return testCycleId;
    }

    private String getIssueId(String jiraId) {
        String issueId = "";
        String responseString = performGetRequest(properties.getProperty(PROPERTY_JIRA_BASE_URL) + APU_URL_JIRA + "issue/" + jiraId);

        if (!responseString.isEmpty()) {
            if ("Zephyr Test".equals(new JSONObject(responseString).getJSONObject("fields")
                    .getJSONObject("issuetype").getString("name"))) {
                issueId = JsonUtils.getJsonValue(responseString, "id", String.class);
                if (issueId.isEmpty()) {
                    LOGGER.warn("Unable to get Issue Id by Jira Id " + jiraId);
                }
            } else {
                LOGGER.warn("Get Issue Id is skipped for " + jiraId + ". Issue type is not Zephyr test");
            }
        } else {
            LOGGER.warn("Get Issue Id is skipped");
        }

        return issueId;
    }

    private String createExecution(String jiraId) {
        String executionId = "";
        String payload = "{\"cycleId\": \"" + testCycleId + "\"," +
                "\"issueId\": \"" + issueId + "\"," +
                "\"projectId\": \"" + projectId + "\"," +
                "\"versionId\": \"" + releaseVersionId + "\"," +
                "\"assigneeType\": \"assignee\"," +
                "\"assignee\": \"" + properties.getProperty(PROPERTY_JIRA_ASSIGNEE) + "\"}";
        String responseString = performPostRequest(properties.getProperty(PROPERTY_JIRA_BASE_URL)
                + API_URL_ZEPHYR + "execution", payload);

        if (!responseString.isEmpty()) {
            JSONObject obj = new JSONObject(responseString);
            executionId = obj.names().get(0).toString();
            if (executionId.isEmpty()) {
                LOGGER.warn("Unable to Create Execution for " + jiraId);
            }
        } else {
            LOGGER.warn("Create Execution skipped");
        }

        return executionId;
    }

    private void updateExecutionDetails(String jiraId, String status) {
        issueId = getIssueId(jiraId);
        if (!issueId.isEmpty()) {
            if (testCycleId.isEmpty()) {
                testCycleId = createTestCycle();
            }

            executionId = createExecution(jiraId);
            if (releaseVersionId.isEmpty() || testCycleId.isEmpty()) {
                LOGGER.warn("Unable to update execution details for " + jiraId + " because of missed data\n" +
                        "(ReleaseVersionId '" + releaseVersionId + "', IssueId '" + issueId + "', " +
                        "TestCycleId '" + testCycleId + "')");
            } else {
                String response = performPutRequest(properties.getProperty(PROPERTY_JIRA_BASE_URL) + API_URL_ZEPHYR +
                        "execution/" + executionId + "/execute", "{\"status\": \"" + status + "\"}");
                if (!response.isEmpty()) {
                    LOGGER.info("Execution Details updated for " + jiraId);
                    if (moveToFolder) {
                        moveExecutionToFolder();
                    }
                }
            }
        } else {
            LOGGER.warn("Update execution details is skipped for " + jiraId);
        }
    }

    private void moveExecutionToFolder() {
        folderId = getTestCycleFolderID();
        if (!folderId.isEmpty()) {
            String payload = "{\"projectId\": " + projectId + "," +
                    "  \"versionId\": " + releaseVersionId + "," +
                    "  \"schedulesList\": [" + executionId + "]}";
            String responseString = performPutRequest(properties.getProperty(PROPERTY_JIRA_BASE_URL) + API_URL_ZEPHYR +
                    "cycle/" + testCycleId + "/move/executions/folder/" + folderId, payload);
            if (new JSONObject(responseString).has("jobProgressToken")) {
                LOGGER.warn("Execution moved to folder '" + folderName + "'");
            } else {
                LOGGER.warn("Unable to move Execution to folder '" + folderName);
            }
        } else {
            LOGGER.warn("Move to folder is skipped");
        }

    }

    private String getTestCycleFolderID() {
        String folderId = "";
        String payload = "{\"cycleId\": " + testCycleId + "," +
                "  \"name\": \"" + folderName + "\"," +
                "  \"description\": \"\"," +
                "  \"projectId\": " + projectId + "," +
                "  \"versionId\": " + releaseVersionId + "," +
                "  \"clonedFolderId\": 0" +
                "}";
        performPostRequest(properties.getProperty(PROPERTY_JIRA_BASE_URL) + API_URL_ZEPHYR + "folder/create", payload);
        String responseString = performGetRequest(properties.getProperty(PROPERTY_JIRA_BASE_URL) + API_URL_ZEPHYR +
                "cycle/" + testCycleId + "/folders?projectId=" + projectId + "&versionId=" + releaseVersionId);

        if (!responseString.isEmpty()) {
            JSONArray arr = new JSONArray(responseString);
            for (Object object : arr) {
                JSONObject obj = (JSONObject) object;
                if (folderName.equals(obj.getString("folderName"))) {
                    folderId = String.valueOf(obj.getInt("folderId"));
                    break;
                }
            }
            if (folderId.isEmpty()) {
                LOGGER.warn("Unable to get Test Cycle Folder Id");
            }
        } else {
            LOGGER.warn("Get Folder Id is skipped");
        }
        return folderId;
    }

    private void parseTestCycleProperty() {
        if (testCycleName.split("/").length > 1) {
            folderName = testCycleName.split("/")[1];
            testCycleName = testCycleName.split("/")[0];
            moveToFolder = true;
            LOGGER.info("Folder for Test Cycle specified with name '" + folderName + "'");
        } else {
            LOGGER.info("Folder for Test Cycle is not specified");
        }
    }

    public void addScenarioResultToCycle(Collection<String> tags, String status) {
        try {
            testCycleName = System.getProperty(SYSTEM_PROPERTY_ZEPHYR_TESTCYCLE_NAME);
            if (testCycleName != null) {
                if (!testCycleName.isEmpty()) {
                    properties = PropertiesReader.read(propertiesFileName, true);
                    if (!properties.getProperty(PROPERTY_JIRA_BASE_URL).isEmpty() &&
                            !properties.getProperty(PROPERTY_JIRA_AUTHORIZATION).isEmpty() &&
                            !properties.getProperty(PROPERTY_JIRA_ASSIGNEE).isEmpty() &&
                            !properties.getProperty(PROPERTY_JIRA_RELEASE_VERSION).isEmpty()) {
                        for (String tag : tags) {
                            if (tag.contains(CUCUMBER_TAG_PATTERN_JIRAID)) {
                                parseTestCycleProperty();
                                tag = tag.replace(CUCUMBER_TAG_PATTERN_JIRAID, "");
                                projectKey = tag.split("-")[0];
                                projectId = getProjectId();
                                releaseVersionId = getReleaseVersionId();
                                testCycleId = getTestCycleId();
                                switch (status) {
                                    case "passed":
                                        updateExecutionDetails(tag, "1");
                                        break;
                                    case "failed":
                                        updateExecutionDetails(tag, "2");
                                        break;
                                    case "skipped":
                                    case "pending":
                                    case "undefined":
                                    case "ambiguous":
                                        updateExecutionDetails(tag, "-1");
                                        break;
                                    default:
                                        LOGGER.warn("Update execution details is skipped for " + tag + ". Reason: status is " + status);
                                        break;
                                }
                            }
                        }
                    } else {
                        LOGGER.warn("Zephyr Update skipped - zephyr.properties is not correctly filled");
                    }
                } else {
                    LOGGER.warn("Zephyr Update skipped - Test Cycle name is empty");
                }
            } else {
                LOGGER.info("Zephyr Update skipped - turned off");
            }
        } catch (Exception e) {
            LOGGER.info("Zephyr Update skipped - unexpected error occurred\n" + e.getMessage());
        }
    }

    private int getExecutionStatus(String executionId) {
        String responseString = performGetRequest(properties.getProperty(PROPERTY_JIRA_BASE_URL) + API_URL_ZEPHYR +
                "execution/" + executionId + "?expand=");
        return JsonUtils.getJsonValue(responseString, "execution.executionStatus", Integer.class);
    }
}
