package com.oneleo.goodsmovementsmanagement.api.test.slotReplenishmentService.countedQuantityMoreThanQuantityToBeMoved;

import static com.oneleo.test.automation.core.ApiUtils.api;
import static com.oneleo.test.automation.core.DatabaseUtils.db;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.oneleo.test.automation.core.TestDataUtils;

import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

public class CountedQuantityMoreThanQuantityToBeMovedStep {
	
	private SqlRowSet rowSet;
	Response val;
	Map<String, String> valueMap = new HashMap<String, String>();
	private ValidatableResponse response;
	
	
	@Given("^The user is a warehouse checker and there is at least one task in Open or Started \"([^\"]*)\"with code \"([^\"]*)\"$")
	public void the_user_is_a_warehouse_checker_and_there_is_at_least_one_task_in_Open_or_Started_with_code(String status, String code) throws Throwable {
		
		String check="select *  from REPLENISHMENT_TASK where code='"+code+"' and status='"+status+"'";
		SqlRowSet rowSet=db().template().queryForRowSet(check);
		rowSet.next();
		
	}
	
	

	@When("^the system scans the Stock Unit linked with code \"([^\"]*)\" and warehouse \"([^\"]*)\" to the product present in the task and with the earliest expiry date  and scans another Stock Unit linked to the product in the task\\. Now the counted quantity is bigger than replenishment order quantity$")
	public void the_system_scans_the_Stock_Unit_linked_with_code_and_warehouse_to_the_product_present_in_the_task_and_with_the_earliest_expiry_date_and_scans_another_Stock_Unit_linked_to_the_product_in_the_task_Now_the_counted_quantity_is_bigger_than_replenishment_order_quantity(String code, String warehouse) throws Throwable {
		valueMap.put("warehouse", warehouse);
		valueMap.put("code", code);
		String jsonRequest=TestDataUtils.filter(valueMap,"com/oneleo/goodsmovementsmanagement/api/test/slotReplenishmentService/countedQuantityMoreThanQuantityToBeMoved/claims.json");
		response = api().template().
				contentType("application/json").
				body(jsonRequest).
				post("/goodsmovementsmanagement/rest/slotreplenishmenthht/claimreplenishmenttask").
				then();
		System.out.println(response);
	}
	
	
	@Then("^The system accepts that the counted quantity with \"([^\"]*)\" is major than replenishment order quantity with \"([^\"]*)\"$")
	public void the_system_accepts_that_the_counted_quantity_with_is_major_than_replenishment_order_quantity_with(String code, String quantity) throws Throwable {

		 String checkToDb="select * from REPLENISHMENT_TASK where code='"+code+"' and quantity='"+quantity+"'";
		 SqlRowSet rowSet=db().template().queryForRowSet(checkToDb);
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
