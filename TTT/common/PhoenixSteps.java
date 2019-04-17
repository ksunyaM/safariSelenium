package com.wba.dataanalytics.api.test.common;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PhoenixSteps {

    private static PhoenixSSHConnector ssh = new PhoenixSSHConnector();

    @Given("^user logins with credentials and connects to phoenix db and fires query \"([^\"]*)\"$")
    public void userLoginsWithCredentialsAndConnectsToPhoenixDbAndFiresQuery(String query) throws Throwable {
        List<String> sparkCommands = new ArrayList<>();
        sparkCommands.add("sudo su - " + ssh.getUserCredentialsForPhoenixDBConn("ssh.sudo"));
        sparkCommands.add("kinit -kt .keytabs/" + ssh.getUserCredentialsForPhoenixDBConn("ssh.sudo") + ".keytab " + ssh.getUserCredentialsForPhoenixDBConn("ssh.sudo"));
        sparkCommands.add("/usr/hdp/2.6.3.0-235/phoenix/bin/sqlline.py " +  ssh.getUserCredentialsForPhoenixDBConn("phoenix.server.1") + "," + ssh.getUserCredentialsForPhoenixDBConn("phoenix.server.2") + "," + ssh.getUserCredentialsForPhoenixDBConn("phoenix.server.3") + ":" + ssh.getUserCredentialsForPhoenixDBConn("port") + ":/hbase-secure");
        sparkCommands.add("!set maxwidth 200");
        sparkCommands.add(query);
        ssh.connectToPhoenixDB();
        ssh.executeCommandsForPhoenixDB(sparkCommands);
        sparkCommands.clear(); //Clear the arraylist so that we dont run the same command again
    }

    @When("^extract output into correct format")
    public List<Map<String, String>> extractOutputIntoCorrectFormat() throws Throwable {
        return PhoenixSSHConnector.extractDataIntoCorrectFormat();
    }

}
