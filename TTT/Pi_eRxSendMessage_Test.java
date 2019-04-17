package com.wba.rxrintegration.api.test.Pi_eRxSendMessage;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {
                "pretty",
                "html:target/Pi_eRxSendMessage",
                "json:target/reports/Pi_eRxSendMessage.json"},
        tags ={"@auto"},
        glue = {"com.wba.rxrintegration.api.test.common","com.wba.rxrintegration.api.test.Pi_eRxSendMessage"}, dryRun = false)
public class Pi_eRxSendMessage_Test {
}
