package com.wba.dataanalytics.api.test.common;

import com.wba.dataanalytics.api.test.common.steps._VerificationStep;
import com.wba.test.utils.ApiUtils;
import com.wba.test.utils.BaseStep;
import com.wba.test.utils.kafka.EventStorage;
import com.wba.dataanalytics.api.test.common.utils.ResourceUtils;
import com.wba.test.utils.kafka.LogUtils;
import cucumber.api.java.Before;
import cucumber.api.java.en.*;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.RestAssured;
import org.junit.Assert;
import com.wba.test.utils.JsonUtils;


import java.util.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.oneleo.test.automation.core.LogUtils.log;

public class AmbariSteps extends BaseStep {
    private static final org.slf4j.Logger LOGGER = log(LogUtils.class);
    private static int numberOfFilesInLandingZone = 0;
    private static int numberOfFilesInRawZone = 0;
    private static final String SELECT = "SELECT * FROM ";
    private static final String DEFAULT_FILES_PATH = "/environment/rxms/data/zone/";
    private static final String LANDING_ZONE_DEFAULT_PATH = "topics/topicName/year=getYear/month=getMonth/day=getDay/hour=getHour";
    private static final String RAW_ZONE_DEFAULT_PATH = "table/year=getYear/month=getMonth/day=getDay";
    private static final String ANALYTICS_FILENAME = "default-analytics.properties";
    private static SSHConnector ssh = new SSHConnector();
    private Map<String, String> param = new HashMap<>();
    public ArrayList<String> hiveData = new ArrayList<>();
    public ArrayList<String> data = new ArrayList<>();

    @Before
    public void init() {
        param.putAll(getAmbariSystemTime());
    }

    @Given("^get initial files from \"([^\"]*)\" zone for topic \"([^\"]*)\"$")
    public void get_initial_files_from_zone(String zone, String topic) {
        String environment = getAnalyticsEnv();
        String path;
        param.put("zone", zone);
        String pathKey = ((zone.equalsIgnoreCase("landing")) ? "topicName" : "table");
        param.put(pathKey, topic);
        path = pathBuilder(environment, param);
        triggerAmbariAPI(path);
        if (zone.equalsIgnoreCase("landing")) {
            numberOfFilesInLandingZone = getFileCount();
            LOGGER.info("Initial number of files in Landing zone for {} = {}", path, numberOfFilesInLandingZone);
        } else {
            // Zone is raw
            numberOfFilesInRawZone = getFileCount();
            LOGGER.info("Initial number of files in Raw zone for {} = {}", path, numberOfFilesInRawZone);
        }
    }

    @Given("^get no of files from \"([^\"]*)\" zone for topic \"([^\"]*)\" after events published$")
    public void get_no_of_files_from_zone_after_events_published(String zone, String topic) {
        String environment = getAnalyticsEnv();
        String path;
        param.put("zone", zone);
        String pathKey = ((zone.equalsIgnoreCase("landing")) ? "topicName" : "table");
        param.put(pathKey, topic);
        path = pathBuilder(environment, param);
        triggerAmbariAPI(path);
        int noOfEventsPublished = Integer.parseInt(dataStorage.unmask("numberofevents").toString());

        if (zone.equalsIgnoreCase("landing")) {
            numberOfFilesInLandingZone = getFileCount();
            int totalNoOfFilesMatch = noOfEventsPublished + numberOfFilesInLandingZone;
            do {
                numberOfFilesInLandingZone = getFileCount();
            } while ((numberOfFilesInLandingZone == totalNoOfFilesMatch));
            LOGGER.info("Initial number of files in Landing zone for {} = {}", path, numberOfFilesInLandingZone);
        } else {
            // Zone is raw
            numberOfFilesInRawZone = getFileCount();
            int totalNoofFilesMatch = noOfEventsPublished + numberOfFilesInLandingZone;
            do {
                numberOfFilesInLandingZone = getFileCount();
            } while ((numberOfFilesInLandingZone == totalNoofFilesMatch));
            LOGGER.info("Initial number of files in Raw zone for {} = {}", path, numberOfFilesInRawZone);
        }
    }

    @When("^hive table contains data for the event \"([^\"]*)\" with correlationId from Kafka$")
    public void userRetrievesDataFromHIVETableWithCorrelationIdFromKafka(String table) throws Throwable {
        String correlationId = EventStorage.getInstance().findEvent(table).getBodyAttribute("correlationId").toString();
        if (correlationId.startsWith("\"")) {
            correlationId = correlationId.replace("\"", "");
        }
        LOGGER.info("CorrelationId - {}", correlationId);
        verifyDataInHiveTable(table, correlationId);
    }

    @When("^hive table contains data for the event \"([^\"]*)\" with correlationIds from Kafka$")
    public void userRetrievesDataFromHIVETableWithCorrelationIdsFromKafka(String table) throws Throwable {
        String[] correlationIdsForHive = dataStorage.unmask("correlationIdsForHive").toString().split(",");
        for (String eventCorrelationId : correlationIdsForHive) {
            eventCorrelationId = eventCorrelationId.trim();
            if (eventCorrelationId.startsWith("[")) {
                eventCorrelationId = eventCorrelationId.replace("[", "");
            }

            if (eventCorrelationId.endsWith("]")) {
                eventCorrelationId = eventCorrelationId.replace("]", "");
            }
            LOGGER.info("CorrelationId - {}", eventCorrelationId);
            verifyDataInHiveTable(table, eventCorrelationId);
        }
    }

    @When("^hive table contains data for the event \"([^\"]*)\"$")
    public void userRetrievesDataFromHIVETableBy(String table) throws Throwable {

        String[] correlationIdsForHive = dataStorage.unmask("correlationIdsForHive").toString().split(",");
        for (String eventCorrelationId : correlationIdsForHive) {
            eventCorrelationId = eventCorrelationId.trim();
            if (eventCorrelationId.startsWith("[")) {
                eventCorrelationId = eventCorrelationId.replace("[", "");
            }

            if (eventCorrelationId.endsWith("]")) {
                eventCorrelationId = eventCorrelationId.replace("]", "");
            }
            LOGGER.info("CorrelationId - {}", eventCorrelationId);
            verifyDataInHiveTable(table, eventCorrelationId);
        }

    }

    public void verifyDataInHiveTable(String table, String eventCorrelationId) throws Throwable {
        String query = SELECT + table + " WHERE " + table + ".correlationid='" + defineValue(eventCorrelationId) + "'";
        RestAssured.useRelaxedHTTPSValidation();
        ApiUtils api = new ApiUtils();
        String expectedStatusCode = "200";
        String responseStatusCode = "";

        Map<String, String> hiveProperties = new HashMap<>();
        hiveProperties.put("job.dataBase", getAnalyticsHiveDbName());
        hiveProperties.put("job.forcedContent", query);

        int loopExecution = 0;
        while (!responseStatusCode.equals(expectedStatusCode) && loopExecution < 10) {
            LOGGER.info("Executing query in Hive DB - {}", query);
            try {
                api.sendApiCall("get_jobs", null, hiveProperties);
                Thread.sleep(30000);
                api.sendApiCall("get_results_ambari", null, Collections.singletonMap("~param", "C.job.id"));
                responseStatusCode = EventStorage.getInstance().getLastConsumed().getHeaders().get("StatusCode");
                if (responseStatusCode.equalsIgnoreCase(expectedStatusCode)) {
                    String hivebody = EventStorage.getInstance().getLastConsumed().getBody().toString();
                    dataStorage.add(eventCorrelationId, hivebody);
                    System.out.println("HIVE Data =========== -" + hivebody);
                }
                LOGGER.info("Response code expected [200] got [{}]", responseStatusCode);
            } catch (Exception exception) {
                LOGGER.warn("Caught Exception - {} ", exception.getMessage());
                if (!exception.getMessage().contains("Result not ready")) {
                    throw new Exception(exception.getMessage());
                }
                LOGGER.warn("Expected exception - Result not ready, will retry...");
            }
            if (responseStatusCode.equals(expectedStatusCode)) {
                int rowSize = EventStorage.getInstance().getLastConsumed().getBodyAttribute("rows.size()");
                Assert.assertEquals(1, rowSize);
                break;
            }
            LOGGER.info("sleeping for 10 seconds for Ambari result to be ready");
            Thread.sleep(10000);
            loopExecution++;
        }
        if (loopExecution == 10) {
            LOGGER.error("Unable to get ambari result even after 10 retries. Response {}", responseStatusCode);
            throw new Exception();
        }
    }

    public void verifyDataInPrepareTable(String table, String count, String query) throws Throwable {
        String dbquery = query;
        RestAssured.useRelaxedHTTPSValidation();
        ApiUtils api = new ApiUtils();
        String expectedStatusCode = "200";
        String responseStatusCode = "";

        Map<String, String> hiveProperties = new HashMap<>();
        hiveProperties.put("job.dataBase", getPrepareZoneHiveDbName());
        hiveProperties.put("job.forcedContent", query);

        int loopExecution = 0;
        while (!responseStatusCode.equals(expectedStatusCode) && loopExecution < 10) {
            LOGGER.info("Executing query in Hive DB - {}", dbquery);
            try {
                api.sendApiCall("get_jobs", null, hiveProperties);
                Thread.sleep(30000);
                api.sendApiCall("get_results_ambari", null, Collections.singletonMap("~param", "C.job.id"));
                responseStatusCode = EventStorage.getInstance().getLastConsumed().getHeaders().get("StatusCode");
                if (responseStatusCode.equalsIgnoreCase(expectedStatusCode)) {
                    String hivebody = EventStorage.getInstance().getLastConsumed().getBody().toString();
                    dataStorage.add(count, hivebody);
                    System.out.println("HIVE Data =========== -" + hivebody);
                }
                LOGGER.info("Response code expected [200] got [{}]", responseStatusCode);
            } catch (Exception exception) {
                LOGGER.warn("Caught Exception - {} ", exception.getMessage());
                if (!exception.getMessage().contains("Result not ready")) {
                    throw new Exception(exception.getMessage());
                }
                LOGGER.warn("Expected exception - Result not ready, will retry...");
            }
            if (responseStatusCode.equals(expectedStatusCode)) {
                int rowSize = EventStorage.getInstance().getLastConsumed().getBodyAttribute("rows.size()");
                if(rowSize >0){
                    Assert.assertTrue(true);
                } else {
                    Assert.assertTrue(false);
                }
                dataStorage.add(count+"_rowsize",rowSize);
                break;
            }
            LOGGER.info("sleeping for 10 seconds for Ambari result to be ready");
            Thread.sleep(10000);
            loopExecution++;
        }
        if (loopExecution == 20) {
            LOGGER.error("Unable to get ambari result even after 10 retries. Response {}", responseStatusCode);
            throw new Exception();
        }
    }

    public void verifyDataInRefDB(String table, String count, String query) throws Throwable {
        String dbquery = query;
        RestAssured.useRelaxedHTTPSValidation();
        ApiUtils api = new ApiUtils();
        String expectedStatusCode = "200";
        String responseStatusCode = "";

        Map<String, String> hiveProperties = new HashMap<>();
        hiveProperties.put("job.dataBase", getAnalyticsRxRefHiveDbName());
        hiveProperties.put("job.forcedContent", query);

        int loopExecution = 0;
        while (!responseStatusCode.equals(expectedStatusCode) && loopExecution < 10) {
            LOGGER.info("Executing query in Hive DB - {}", dbquery);
            try {
                api.sendApiCall("get_jobs", null, hiveProperties);
                Thread.sleep(30000);
                api.sendApiCall("get_results_ambari", null, Collections.singletonMap("~param", "C.job.id"));
                responseStatusCode = EventStorage.getInstance().getLastConsumed().getHeaders().get("StatusCode");
                if (responseStatusCode.equalsIgnoreCase(expectedStatusCode)) {
                    String hivebody = EventStorage.getInstance().getLastConsumed().getBody().toString();
                    dataStorage.add(count, hivebody);
                    System.out.println("HIVE Data =========== -" + hivebody);
                }
                LOGGER.info("Response code expected [200] got [{}]", responseStatusCode);
            } catch (Exception exception) {
                LOGGER.warn("Caught Exception - {} ", exception.getMessage());
                if (!exception.getMessage().contains("Result not ready")) {
                    throw new Exception(exception.getMessage());
                }
                LOGGER.warn("Expected exception - Result not ready, will retry...");
            }
            if (responseStatusCode.equals(expectedStatusCode)) {
                int rowSize = EventStorage.getInstance().getLastConsumed().getBodyAttribute("rows.size()");
                dataStorage.add(count+"_rowsize",rowSize);
                break;
            }
            LOGGER.info("sleeping for 10 seconds for Ambari result to be ready");
            Thread.sleep(10000);
            loopExecution++;
        }
        if (loopExecution == 20) {
            LOGGER.error("Unable to get ambari result even after 10 retries. Response {}", responseStatusCode);
            throw new Exception();
        }
    }

    @When("^user verifies a new file created for topic \"([^\"]*)\" in \"([^\"]*)\" zone$")
    public void userVerifiesZoneWithData(String eventOrTopicName, String zone) throws Throwable {
        String environment = getAnalyticsEnv();
        if (zone.equalsIgnoreCase("landing")) {
            param.put("topicName", eventOrTopicName);
        } else if (zone.equalsIgnoreCase("raw")) {
            param.put("table", eventOrTopicName);
        } else {
            param.put("table", eventOrTopicName);
        }
        if (zone.equalsIgnoreCase("landing") || zone.equalsIgnoreCase("raw")) {
            param.put("zone", zone);
            String path = pathBuilder(environment, param);
            triggerAmbariAPI(path);
            verifyNumberOfFiles(zone);
        }
    }

    private Integer getFileCount() {
        int fileCount;
        try {
            if (EventStorage.getInstance().getLastConsumed().getBody().contains("File does not exist")) {
                fileCount = 0;
            } else {
                LOGGER.info(EventStorage.getInstance().getLastConsumed().getBodyAttribute("files").toString());
                fileCount = EventStorage.getInstance().getLastConsumed().getBodyAttribute("files.size()");
                LOGGER.info("File Count - {}", fileCount);
            }
        } catch (RuntimeException rte) {
            LOGGER.warn(rte.getMessage());
            fileCount = 0;
        }
        return fileCount;
    }

    private void verifyNumberOfFiles(String zone) {
        if (zone.equalsIgnoreCase("landing")) {
            new _VerificationStep().verify_that("C.files.size()", ">", String.valueOf(numberOfFilesInLandingZone));
        } else {
            new _VerificationStep().verify_that("C.files.size()", ">", String.valueOf(numberOfFilesInRawZone));
        }
        int initialCount = zone.equalsIgnoreCase("landing") ? numberOfFilesInLandingZone : EventStorage.getInstance().getLastConsumed().getBodyAttribute("files.size()");
        LOGGER.info("Verification in {} zone - PASSED:\n" +
                        "Inital number of files={}\n" +
                        "Final number of files={}", zone, initialCount,
                EventStorage.getInstance().getLastConsumed().getBodyAttribute("files.size()")
        );
    }

    private void triggerAmbariAPI(String path) {
        try {
            RestAssured.useRelaxedHTTPSValidation();
            new ApiUtils().sendApiCall("get_data_from_filetree", null, Collections.singletonMap("~param", path));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    private Map<String, String> getAmbariSystemTime() {
        return DateHelper.convertSystemTimeToGmtAmbariTime(new Date());
    }


    @When("^user executes spark-submit command on the \"([^\"]*)\" hour")
    public void userExecutesSparkCommand(String hour) {
        /* We need to run spark submit job twice
            1. For the zero hour partition which triggers data for the all hours
            2. For the hour partition where the landing zone data exists
        */
        List<String> sparkCommands = new ArrayList<>();

        sparkCommands.add("sudo su - " + ssh.getProperty("ssh.sudo"));
        sparkCommands.add("kinit -kt .keytabs/" + ssh.getProperty("ssh.sudo") + ".keytab " + ssh.getProperty("ssh.sudo"));
        sparkCommands.add(getSparkCommand(hour));
        ssh.connectToAmbari();
        ssh.executeCommands(sparkCommands);
        sparkCommands.clear(); //Clear the arraylist so that we dont run the same command again
    }

    @Then("^event is not present in landing zone for topic \"([^\"]*)\"$")
    public void event_is_not_present_in_landing_zone_for_topic(String topicName) throws Throwable {
        param.put("zone", "landing");
        param.put("topicName", topicName);
        String environment = getAnalyticsEnv();

        String path = pathBuilder(environment, param);
        triggerAmbariAPI(path);

        try {
            EventStorage.getInstance().getLastConsumed().getHeaders().get("StatusCode");
        } catch (RuntimeException rte) {
            LOGGER.warn("Caught exception - {} ", rte.getMessage());
            if (rte.getMessage().contains("Consumed event is not found by name: null")) {
                LOGGER.info("Expected exception");
            } else {
                throw new RuntimeException(rte.getMessage());
            }
        }
    }

    private String getSparkCommand(String hour) {
        Map<String, String> properties;
        properties = ResourceUtils.getPropertiesFileAsMap(ANALYTICS_FILENAME);

        String env = properties.get("analytics.env");
        String sparkSubmitCommand = properties.get("spark.command");
        String sparkCommandSuffix = properties.get("spark.command.suffix");
        if (hour.equals("00")) {
            LOGGER.info("Running spark-submit job in the {} environment on the zero hour partition", env.toUpperCase());
            sparkSubmitCommand += " year=" + param.get("year") + "/month=" + param.get("month") + "/day=" + param.get("day") + "/hour=00 " + sparkCommandSuffix;
        } else {
            LOGGER.info("Running spark-submit job in the {} environment on the {} hour partition", env.toUpperCase(), param.get("hour"));
            sparkSubmitCommand += " year=" + param.get("year") + "/month=" + param.get("month") + "/day=" + param.get("day") + "/hour=" + param.get("hour") + " " + sparkCommandSuffix;
        }
        LOGGER.debug("Executing command - {}", sparkSubmitCommand);
        return sparkSubmitCommand;
    }

    private String defaultPathConvertor(String environment, String zone) {
        return DEFAULT_FILES_PATH.replace("environment", environment).replace("zone", zone);
    }

    private String pathBuilder(String environment, Map param) {
        String path = defaultPathConvertor(environment, param.get("zone").toString());
        switch (param.get("zone").toString()) {
            case "landing": {
                return path + LANDING_ZONE_DEFAULT_PATH
                        .replace("topicName", param.get("topicName").toString())
                        .replace("getYear", param.get("year").toString())
                        .replace("getMonth", param.get("month").toString())
                        .replace("getDay", param.get("day").toString())
                        .replace("getHour", param.get("hour").toString());
            }
            case "raw": {
                return path + RAW_ZONE_DEFAULT_PATH
                        .replace("table", param.get("table").toString())
                        .replace("getYear", param.get("year").toString())
                        .replace("getMonth", param.get("month").toString())
                        .replace("getDay", param.get("day").toString());
            }
            case "prepare": {
                return path + RAW_ZONE_DEFAULT_PATH
                        .replace("table", param.get("table").toString())
                        .replace("getYear", param.get("year").toString())
                        .replace("getMonth", param.get("month").toString())
                        .replace("getDay", param.get("day").toString());
            }
            default:
                LOGGER.info("No zone with name " + param.get("zone").toString() + " was found");
                return null;
        }
    }

    private String getAnalyticsEnv() {
        Map<String, String> properties;
        properties = ResourceUtils.getPropertiesFileAsMap("default-analytics.properties");
        return properties.get("analytics.env");
    }

    private String getAnalyticsHiveDbName() {
        return ResourceUtils.getPropertiesFileAsMap("default-analytics.properties").get("hiveDbName");
    }

    private String getAnalyticsRxRefHiveDbName() {
        return ResourceUtils.getPropertiesFileAsMap("default-analytics.properties").get("refhiveDbName");
    }

    private String getPrepareZoneHiveDbName() {
        return ResourceUtils.getPropertiesFileAsMap("default-analytics.properties").get("preparehiveDbName");
    }

}
