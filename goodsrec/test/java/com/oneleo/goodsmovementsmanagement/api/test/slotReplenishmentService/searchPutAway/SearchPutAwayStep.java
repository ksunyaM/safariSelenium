package com.oneleo.goodsmovementsmanagement.api.test.slotReplenishmentService.searchPutAway;


import static com.oneleo.test.automation.core.ApiUtils.api;
import static com.oneleo.test.automation.core.DatabaseUtils.db;

import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.oneleo.test.automation.core.TestDataUtils;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

public class SearchPutAwayStep {
	
	private SqlRowSet rowSet;
	Response val;
	Map<String, String> valueMap = new HashMap<String, String>();
	private ValidatableResponse response;
	
	@Before
	public void insertBeforeProcessing() throws Exception{
		String code = "TESTALFO";
		valueMap.put("code", code);
		String queryInsert = TestDataUtils.filter(valueMap,
				"com/oneleo/goodsmovementsmanagement/api/test/slotReplenishmentService/ReplenishmentTaskSearchbyStockUnit/deleteBeforeProcess.sql");
		db().template().execute(queryInsert);
	}
	
	@Given("^There are more replenishment tasks with code \"([^\"]*)\" in the system with stock \"([^\"]*)\"$")
	public void there_are_more_replenishment_tasks_with_code_in_the_system_with_stock(String code, String status) throws Throwable {
		valueMap.put("code", code);
		String queryInsert = TestDataUtils.filter(valueMap,
				"com/oneleo/goodsmovementsmanagement/api/test/slotReplenishmentService/ReplenishmentTaskSearchbyStockUnit/insertGoodsMovements.sql");
		db().template().execute(queryInsert);
		String checkReplenishment = "select * from REPLENISHMENT_TASK where code= '" + code + "'";
		SqlRowSet rowSet = db().template().queryForRowSet(checkReplenishment);
		rowSet.next();
	
	}

	@When("^The system search for replenishment tasks with slot \"([^\"]*)\" warehouse \"([^\"]*)\" and \"([^\"]*)\"$")
	public void the_system_search_for_replenishment_tasks_with_slot_warehouse_and(String slot, String warehouse, String status) throws Throwable {
		valueMap.put("slot", slot);
		valueMap.put("warehouse", warehouse);
		valueMap.put("status", status);
		String jsonRequest = TestDataUtils.filter(valueMap,
				"/com/oneleo/goodsmovementsmanagement/api/test/slotReplenishmentService/searchPutAway/readRepl.json");
		response = api().template().contentType("application/json").body(jsonRequest)
				.post("/goodsmovementsmanagement/rest/slotreplenishmenthht/readreplenishmenttasklist").then();//.statusCode(200); 
	}

	@Then("^the system performs the search with stock as search criteria \"([^\"]*)\"$")
	public void the_system_performs_the_search_with_stock_as_search_criteria(String code) throws Throwable {
		String checkReplenishment = "select * from stock_group where code= '" + code + "'";
		SqlRowSet rowSet = db().template().queryForRowSet(checkReplenishment);
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
