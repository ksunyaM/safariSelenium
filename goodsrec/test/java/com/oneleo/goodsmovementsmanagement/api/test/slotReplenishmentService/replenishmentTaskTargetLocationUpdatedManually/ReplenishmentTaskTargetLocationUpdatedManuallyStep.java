package com.oneleo.goodsmovementsmanagement.api.test.slotReplenishmentService.replenishmentTaskTargetLocationUpdatedManually;

import static com.oneleo.test.automation.core.ApiUtils.api;
import static com.oneleo.test.automation.core.DatabaseUtils.db;
import java.util.HashMap;
import java.util.Map;
import org.junit.After;
import org.junit.Assert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import com.oneleo.test.automation.core.TestDataUtils;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.ValidatableResponse;

public class ReplenishmentTaskTargetLocationUpdatedManuallyStep {
	
	Map<String, String> valueMap = new HashMap<String, String>();
	private ValidatableResponse response;
	String warehouse="3701";
	String code="";
	
	@Given("^There is at least one task in \"([^\"]*)\" status with code \"([^\"]*)\"$")
	public void there_is_at_least_one_task_in_status_with_code(String code, String status) throws Throwable {
	
		String check="select *  from REPLENISHMENT_TASK where code='"+code+"' and status='"+status+"'";
		SqlRowSet rowSet=db().template().queryForRowSet(check);
		rowSet.next();

	}

	@When("^the system displays a warning message and indicates that the max quantity should be: Replenishment Qty-Qty in the slot$")
	public void the_system_displays_a_warning_message_and_indicates_that_the_max_quantity_should_be_Replenishment_Qty_Qty_in_the_slot() throws Throwable {

		valueMap.put("warehouse", warehouse);
		valueMap.put("code", code);
		String jsonRequest=TestDataUtils.filter(valueMap,"com/oneleo/goodsmovementsmanagement/api/test/slotReplenishmentService/replenishmentTaskTargetLocationUpdatedManually/claims.json");
		response = api().template().
				contentType("application/json").
				body(jsonRequest).
				post("/goodsmovementsmanagement/rest/slotreplenishmenthht/claimreplenishmenttask").
				then();
	//	System.out.println(response);
		
	}

	@Then("^The system displays the warning message with the correct quantity to work$")
	public void the_system_displays_the_warning_message_with_the_correct_quantity_to_work() throws Throwable {
		
	}
	@After
	public void deleteProcessing() throws Exception{
	String code="test1";
	valueMap.put("code", code);
	String querydeletePreProcessinig = TestDataUtils.filter(valueMap,"com/oneleo/goodsmovementsmanagement/api/test/slotReplenishmentService/changeStatus/deleteRepelnishment.sql");
	db().template().execute(querydeletePreProcessinig);
	}
	
}
