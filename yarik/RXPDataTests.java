package com.wba.rxdata.test;

import com.oneleo.test.automation.core.annotation.Api;
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;
@RunWith(Cucumber.class)
@CucumberOptions(
       // monochrome = true,
       // features = "C:\\Workspace\\rxpdata\\acceptance-test\\api-test\\src\\test\\resources\\com\\wba\\rxdata\\test",
        plugin = {"json:target/reports/RxDataAPI.json"}//,
        //tags={"@auto"}
        )
       // format={"pretty","html:target/cucmber-html-reports"})
@Api
public class RXPDataTests {
}