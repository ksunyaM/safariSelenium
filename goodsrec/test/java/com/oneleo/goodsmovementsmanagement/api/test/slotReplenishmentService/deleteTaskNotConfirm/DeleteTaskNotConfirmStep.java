package com.oneleo.goodsmovementsmanagement.api.test.slotReplenishmentService.deleteTaskNotConfirm;


import static com.oneleo.test.automation.core.ApiUtils.api;
import static com.oneleo.test.automation.core.DatabaseUtils.db;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.oneleo.test.automation.core.TestDataUtils;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.ValidatableResponse;

public class DeleteTaskNotConfirmStep {
	
	private Map<String, String> valueMap = new HashMap<String, String>();
	private ValidatableResponse response;
	
	@Before
	public void insertPreProcessing() throws Exception{
		
		String code="test1";
		valueMap.put("code", code);
		String queryInsertPreProcessinig = TestDataUtils.filter(valueMap,"com/oneleo/goodsmovementsmanagement/api/test/slotReplenishmentService/deleteTaskConfirm/insertReplenishmentTask.sql");
		db().template().execute(queryInsertPreProcessinig);
		
	}
	
	@Given("^There is at least one task with \"([^\"]*)\" in Open or Started \"([^\"]*)\"in the system$")
	public void there_is_at_least_one_task_with_in_Open_or_Started_in_the_system(String code, String status) throws Throwable {
		String checkReplenishment="select * from REPLENISHMENT_TASK where code= '"+code+"' and status='"+status+"'";
		SqlRowSet rowSet=db().template().queryForRowSet(checkReplenishment);
		rowSet.next();
	}

	@When("^the system delete the replenishment task with code \"([^\"]*)\", status \"([^\"]*)\" and warehouse \"([^\"]*)\"$")
	public void the_system_delete_the_replenishment_task_with_code_status_and_warehouse(String code, String status, String warehouse) throws Throwable {
		valueMap.put("status", status);
		String jsonRequest=TestDataUtils.filter(valueMap,"com/oneleo/goodsmovementsmanagement/api/test/slotReplenishmentService/deleteTaskConfirm/read.json");
		System.out.println(jsonRequest);
		response = api().template().
				contentType("application/json").
				body(jsonRequest).
				post("/goodsmovementsmanagement/rest/slotreplenishmenthht/readreplenishmenttasklist").
				then().statusCode(200);
		valueMap.put("code", code);
		valueMap.put("warehouse", warehouse);
		
		String jsonRequest2=TestDataUtils.filter(valueMap,"com/oneleo/goodsmovementsmanagement/api/test/slotReplenishmentService/deleteTaskConfirm/claim.json");
		System.out.println(jsonRequest2);
		response = api().template().
				contentType("application/json").
				body(jsonRequest2).
				post("/goodsmovementsmanagement/rest/slotreplenishmenthht/claimreplenishmenttask").
				then().statusCode(200);
		
		
	}

	@Then("^The system displays a confirm deletion message\\. The user doesn't confirm the action and the system doesn't delete the task with \"([^\"]*)\"$")
	public void the_system_displays_a_confirm_deletion_message_The_user_doesn_t_confirm_the_action_and_the_system_doesn_t_delete_the_task_with(String code) throws Throwable {

		String checkDbAfterDelete="select * from REPLENISHMENT_TASK where code= '"+code+"'";
		SqlRowSet rowSet=db().template().queryForRowSet(checkDbAfterDelete);
		assertTrue(rowSet.next());
	}
	
	@After 
	public void deleteAfterProcessing() throws Exception{
		String code="test1";
		valueMap.put("code", code);
		String queryAfterProcessinig = TestDataUtils.filter(valueMap,"com/oneleo/goodsmovementsmanagement/api/test/slotReplenishmentService/deleteTaskNotConfirm/deleteAfterProcess.sql");
		db().template().execute(queryAfterProcessinig);
	}
	@After
	public void deleteProcessing() throws Exception{
	String code="test1";
	valueMap.put("code", code);
	String querydeletePreProcessinig = TestDataUtils.filter(valueMap,"com/oneleo/goodsmovementsmanagement/api/test/slotReplenishmentService/changeStatus/deleteRepelnishment.sql");
	db().template().execute(querydeletePreProcessinig);
	}

}
