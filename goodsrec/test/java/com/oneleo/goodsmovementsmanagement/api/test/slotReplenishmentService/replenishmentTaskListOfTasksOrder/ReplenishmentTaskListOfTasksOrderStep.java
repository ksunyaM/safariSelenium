package com.oneleo.goodsmovementsmanagement.api.test.slotReplenishmentService.replenishmentTaskListOfTasksOrder;

import static com.oneleo.test.automation.core.ApiUtils.api;
import static com.oneleo.test.automation.core.DatabaseUtils.db;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import com.oneleo.test.automation.core.TestDataUtils;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.ValidatableResponse;

public class ReplenishmentTaskListOfTasksOrderStep {

	private Map<String, String> valueMap = new HashMap<String, String>();
	private ValidatableResponse response;


	@Given("^There are more replenishment tasks in the system with status \"([^\"]*)\"$")
	public void there_are_more_replenishment_tasks_in_the_system_with_status(String status) throws Throwable {

		String check = "select count(*) from REPLENISHMENT_TASK where status='" + status + "'";
		SqlRowSet rowSet = db().template().queryForRowSet(check);
		rowSet.next();
		int numberOfTaskOpen = rowSet.getInt(1);
//		Assert.assertTrue(numberOfTaskOpen>1);

	}

	@When("^the system check that the system search all the replenishment tasks with status \"([^\"]*)\" and warehouse \"([^\"]*)\"$")
	public void the_system_check_that_the_system_search_all_the_replenishment_tasks_with_status_and_warehouse(String status, String warehouse) throws Throwable {

	
		valueMap.put("status", status);
		valueMap.put("warehouse", warehouse);
		String jsonRequest = TestDataUtils.filter(valueMap,
				"com/oneleo/goodsmovementsmanagement/api/test/slotReplenishmentService/replenishmentTaskListOfTasksOrder/readReplenishment.json");
		response = api().template().contentType("application/json").body(jsonRequest)
				.post("/goodsmovementsmanagement/rest/slotreplenishmenthht/readreplenishmenttasklist").then().statusCode(200);

	}

	@Then("^The system displays the replenishment tasks with status \"([^\"]*)\"$")
	public void the_system_displays_the_replenishment_tasks_with_status(String status) throws Throwable {

		String check = "select count(*) from REPLENISHMENT_TASK where status='" + status + "'";
		SqlRowSet rowSet = db().template().queryForRowSet(check);
		rowSet.next();
		int numberOfTaskOpen = rowSet.getInt(1);
		//Assert.assertTrue(numberOfTaskOpen>1);
	}
	
	@After
	public void deleteProcessing() throws Exception{
	String code="test1";
	valueMap.put("code", code);
	String querydeletePreProcessinig = TestDataUtils.filter(valueMap,"com/oneleo/goodsmovementsmanagement/api/test/slotReplenishmentService/changeStatus/deleteRepelnishment.sql");
	db().template().execute(querydeletePreProcessinig);
	}

}
