package com.oneleo.goodsmovementsmanagement.api.test.slotReplenishmentService.changeStatus;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = { "json:target/reports/ChangeStatus.json" })
public class ChangeStatusTest {

}
