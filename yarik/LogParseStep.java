package com.wba.rxdata.test;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import org.junit.Assert;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.sql.Timestamp;
import static org.hamcrest.Matchers.is;


public class LogParseStep extends  _BaseStep{

    private static final String KUBERNETES_SERVER = LogHandler.getInstance().getValue("log.server");
    private static final Logger LOGGER = com.oneleo.test.automation.core.LogUtils.log(LogParseStep.class);


    private final String KIT_NAME = LogHandler.getInstance().getValue("kit.name");
    private static final String CONTAINER_NAME = LogHandler.getInstance().getValue("container.name");
    private static Map<String, Boolean> msgs = new HashMap<>();
    public List<String> GetApplicationLogs(String sinceTime) {
        List<String> logsString = new ArrayList<String>();

        String currentPod = getCurrentNode(KIT_NAME);
        try {

            URL url = new URL(String.format("%s/api/v1/namespaces/" + KIT_NAME.split("-")[1] + "/pods/%s/log?container=%s&sinceTime=%s", KUBERNETES_SERVER, currentPod, CONTAINER_NAME, sinceTime));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));
            String output;
            while ((output = br.readLine()) != null) {
                logsString.add(output);
            }
            conn.disconnect();
        } catch (IOException e) {
            LOGGER.warn("Unable to read topic metadata: {}", e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
        return logsString;
    }


    @And("^wait till log value \"([^\"]*)\"$")
    public void waitTillLogValue(String arg0) throws Throwable {
//        List<String> logs = GetApplicationLogs(dataStorage.unmask("~SINCE_TIME").toString());
        int retry = 1 * 20;
        int trynumber = 0;
        boolean foundLog = false;
        while (trynumber++ < retry && !foundLog) {
            foundLog = GetApplicationLogs(new Timestamp(asMillSec(dataStorage.unmask("~SINCE_TIME").toString())).toInstant().toString())
                    .stream()
                    .anyMatch(item -> (item.contains(arg0) || item.contains("java.net.SocketTimeoutException")));
            LOGGER.info("Check for log : {}", foundLog);
            Thread.sleep(1000);
        }
    }


    @And("^passing explicit time of \"([^\"]*)\" second$")
    public void passingExplicitTimeOfSecond(Integer  sec) throws Throwable {
        LOGGER.debug("Going to wait " + sec + " second(s)");
        try {
            Thread.sleep(sec * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
      @Then("^verify the following messages: \"([^\"]*)\" in the application log$")
       public void verify_the_following_messages_in_the_application_log(String msg) {
        verifyAppLogs(msg, dataStorage.unmask("~SINCE_TIME").toString(), true);
        }

    @Then("^verify the following string with CorrelationId: \"([^\"]*)\" in the application log$")
    public void verifyTheFollowingStringWithCorrelationIdInTheApplicationLog(String msg) throws Throwable {
        List<String> aaplogList = verifyAppLogs((String) dataStorage.unmask("uniqueID"),
                dataStorage.unmask("~SINCE_TIME").toString(), true);

        Assert.assertThat(aaplogList.stream().anyMatch(expectedString->expectedString.contains(msg)),is(true));
    }


    private String getCurrentNode(String kitName) {
        String currentPod = "";
        Configuration conf = Configuration.defaultConfiguration();
        try {
            URL url = new URL(String.format("%s/api/v1/namespaces/devqe/pods", KUBERNETES_SERVER));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));
            String pattern1 = "^(?=.*\\b{KitName}\\b).*$";
            String pattern2 = "^(?:(?!\\-mock-).)*$\\r?\\n?";
            String output;
            while ((output = br.readLine()) != null) {
                List<String> jsonPaths = JsonPath.using(conf).parse(output).read("$.items[*].metadata.name");
                for (String path : jsonPaths) {
                    if (path.matches(pattern1.replace("{KitName}", kitName)) && path.matches(pattern2)) {
                        currentPod = path;
                        System.out.println(currentPod);
                    }
                }
            }
            conn.disconnect();
        } catch (IOException e) {
            LOGGER.warn("Unable to read topic metadata: {}", e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
        return currentPod;
    }


    private List<String> verifyAppLogs(String msg, String sinceTime, boolean isPresent) {
        List<String> foundString = new ArrayList<String>();
        String currentPod = getCurrentNode(KIT_NAME);

        for (String outer : splitBy(msg, "||")) {
            msgs.put(outer, false);
        }
        int counter = msgs.size();
        try {

            URL url = new URL(String.format("%s/api/v1/namespaces/devqe/pods/%s/log?container=%s&sinceTime=%s", KUBERNETES_SERVER, currentPod, CONTAINER_NAME, sinceTime));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));
            String output;
            int iterator = 0;
            while ((output = br.readLine()) != null) {
                if (containsInMap(output)) {
                    iterator++;
                    foundString.add(output);
                    if (iterator >= counter) break;
                }
            }


            Iterator iterator1 = msgs.entrySet().iterator();
            while (iterator1.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator1.next();
                if (isPresent) {
                   Assert.assertTrue("Message " + entry.getKey() + "is present in the application log", (Boolean) entry.getValue());
                } else {
                    Assert.assertFalse("Message " + entry.getKey() + "is not present in the application log", (Boolean) entry.getValue());
                }
                iterator1.remove();
            }

       } catch (IOException e) {
            LOGGER.warn("Unable to read topic metadata: {}", e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
        return foundString;
    }

    private static List<String> splitBy(String toSplit, String delimiter) {
        List<String> tokens = new ArrayList<>();
        StringTokenizer tokenizer = new StringTokenizer(toSplit, delimiter);
        while (tokenizer.hasMoreTokens()) {
            tokens.add(tokenizer.nextToken());
        }
        return tokens;
    }

    private static boolean containsInMap(String log) {
        Iterator iterator = msgs.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            System.out.println(entry.getKey() + " = " + entry.getValue());
            if (log.contains(entry.getKey().toString())) {
                msgs.put(entry.getKey().toString(), true);
                return true;
            }
            iterator.remove();
        }
        return false;
    }

}
