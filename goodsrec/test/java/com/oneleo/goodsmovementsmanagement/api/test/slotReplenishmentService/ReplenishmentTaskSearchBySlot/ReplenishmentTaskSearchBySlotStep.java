package com.oneleo.goodsmovementsmanagement.api.test.slotReplenishmentService.ReplenishmentTaskSearchBySlot;

import static com.oneleo.test.automation.core.ApiUtils.api;
import static com.oneleo.test.automation.core.DatabaseUtils.db;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.oneleo.test.automation.core.TestDataUtils;

import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.ValidatableResponse;

public class ReplenishmentTaskSearchBySlotStep {
	Map<String, String> valueMap = new HashMap<String, String>();
	private ValidatableResponse response;
	
	@Before
	public void deleteBeforProcess() throws Exception{
		
		String code="TESTALFO";
		valueMap.put("code", code);
		String queryDelete = TestDataUtils.filter(valueMap,"com/oneleo/goodsmovementsmanagement/api/test/slotReplenishmentService/ReplenishmentTaskSearchBySlot/deleteBeforeProcess.sql");
		db().template().execute(queryDelete);
		
	}
	
	@Given("^There are more replenishment tasks in the system with slot \"([^\"]*)\"$")
	public void there_are_more_replenishment_tasks_in_the_system_with_slot(String slot) throws Throwable {
			
		String checkReplenishment="select * from REPLENISHMENT_TASK where code= '"+slot+"'";
		SqlRowSet rowSet=db().template().queryForRowSet(checkReplenishment);
		rowSet.next();
	}

	@When("^The system search for replenishment tasks with \"([^\"]*)\" , status \"([^\"]*)\" and \"([^\"]*)\"$")
	public void the_system_search_for_replenishment_tasks_with_status_and(String slot,String status,String warehouse) throws Throwable {
		valueMap.put("slot",slot);
		valueMap.put("status", status);
		valueMap.put("warehouse", warehouse);
		String jsonRequest=TestDataUtils.filter(valueMap,"com/oneleo/goodsmovementsmanagement/api/test/slotReplenishmentService/ReplenishmentTaskSearchBySlot/searchSlots.json");
		System.out.println(jsonRequest);
		
		response = api().template().
				contentType("application/json").
				body(jsonRequest).
				post("/goodsmovementsmanagement/rest/slotreplenishmenthht/readreplenishmenttasklist").
				then().statusCode(200);	
		
	}

	@Then("^the system performs the search with slot with \"([^\"]*)\"$")
	public void the_system_performs_the_search_with_slot_with(String slot) throws Throwable {
		String checkReplenishment="select * from slot where code= '"+slot+"'";
		SqlRowSet rowSet=db().template().queryForRowSet(checkReplenishment);
		rowSet.next();
	}
	@After
	public void deleteProcessing() throws Exception{
	String code="test1";
	valueMap.put("code", code);
	String querydeletePreProcessinig = TestDataUtils.filter(valueMap,"com/oneleo/goodsmovementsmanagement/api/test/slotReplenishmentService/changeStatus/deleteRepelnishment.sql");
	db().template().execute(querydeletePreProcessinig);
	}
	
}
