package com.oneleo.goodsmovementsmanagement.api.test.slotReplenishmentService.detailsButton;

import static com.oneleo.test.automation.core.ApiUtils.api;
import static com.oneleo.test.automation.core.DatabaseUtils.db;
import static io.restassured.path.json.JsonPath.from;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import org.junit.Assert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import com.oneleo.test.automation.core.TestDataUtils;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

public class DetailsButtonStep {
	
	private SqlRowSet rowSet;
	Response val;
	Map<String, String> valueMap = new HashMap<String, String>();
	private ValidatableResponse response;
	String sourceLocation;
	
//	@Before
//	public void insertPreProcess() throws Exception{
//		String code="TESTGDS";
//		valueMap.put("code", code);
//		String insert = TestDataUtils.filter(valueMap,"com/oneleo/goodsmovementsmanagement/api/test/slotReplenishmentService/detailsButton/insertBeforeProcessing.sql");
//		db().template().execute(insert);
//	}
	
	@Given("^There is at least one RT with code \"([^\"]*)\" in Open status \"([^\"]*)\" in the system$")
	public void there_is_at_least_one_RT_with_code_in_Open_status_in_the_system(String code, String status) throws Throwable {
		
		String checkToDb="select * from REPLENISHMENT_TASK where code='"+code+"'";
		SqlRowSet rowSet2=db().template().queryForRowSet(checkToDb);
		rowSet2.next();
		
	}
	
	@When("^the system check the button details for the replenishment task with code \"([^\"]*)\", status \"([^\"]*)\" and warehouse \"([^\"]*)\"$")
	public void the_system_check_the_button_details_for_the_replenishment_task_with_code_status_and_warehouse(String code, String status, String warehouse) throws Throwable {
	 	
		valueMap.put("code", code);
		valueMap.put("status", status);
		valueMap.put("warehouse", warehouse);
		String jsonRequest=TestDataUtils.filter(valueMap,"/com/oneleo/goodsmovementsmanagement/api/test/slotReplenishmentService/detailsButton/detailsButton.json");
		System.out.println(jsonRequest);
		response = api().template().
				contentType("application/json").
				body(jsonRequest).
				post("/goodsmovementsmanagement/rest/slotreplenishmenthht/readreplenishmenttasklist").
				then().statusCode(200);	
		
	}


	@Then("^The system opens the UI with information about the \"([^\"]*)\"$")
	public void the_system_opens_the_UI_with_information_about_the(String arg1) throws Throwable {
	  
	    String responseServiceAsStringtemp = response.extract().asString();
		List<String> countSlot =from(responseServiceAsStringtemp).get("listReplenishmentTaskHHTTO.listReplenishmentTaskLocationHHTTO.sourceLocation");
		Assert.assertEquals(1, countSlot.size());
		
	}
	
//	@After
//	public void deleteProcess() throws Exception{
//		
//		String code="TESTGDS";
//		valueMap.put("code", code);
//		String deleteProcess = TestDataUtils.filter(valueMap,"com/oneleo/goodsmovementsmanagement/api/test/slotReplenishmentService/detailsButton/deleteRepelnishment.sql");
//		db().template().execute(deleteProcess);
//		
//	}
	
	
	

}
