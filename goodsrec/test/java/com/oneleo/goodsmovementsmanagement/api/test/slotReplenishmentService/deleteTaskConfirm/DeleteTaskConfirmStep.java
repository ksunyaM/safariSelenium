package com.oneleo.goodsmovementsmanagement.api.test.slotReplenishmentService.deleteTaskConfirm;


import static com.oneleo.test.automation.core.ApiUtils.api;
import static com.oneleo.test.automation.core.DatabaseUtils.db;
import static io.restassured.path.json.JsonPath.from;
import static org.junit.Assert.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.oneleo.test.automation.core.TestDataUtils;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.ValidatableResponse;
import org.junit.Assert;
public class DeleteTaskConfirmStep {
	
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
	
	valueMap.put("code", code);
	valueMap.put("warehouse", warehouse);
	
	String jsonRequest3=TestDataUtils.filter(valueMap,"com/oneleo/goodsmovementsmanagement/api/test/slotReplenishmentService/deleteTaskConfirm/delete.json");
	System.out.println(jsonRequest3);
	response = api().template().
			contentType("application/json").
			body(jsonRequest3).
			post("/goodsmovementsmanagement/rest/slotreplenishmenthht/deletereplenishmenttask").
			then().statusCode(200);
	
}

@Then("^The system displays a confirm deletion message$")
public void the_system_displays_a_confirm_deletion_message() throws Throwable {
	String responseServiceAsString = response.extract().asString();
	List<String> resultNodes = from(responseServiceAsString).getList("serviceResult.resultMessages.findAll {it.messageDefault =='Delete Selected Task success.'}");
	Assert.assertEquals(1, resultNodes.size());
}

@Then("^The user confirms the action and the system delete phisically the task with check on db for the delete task with code \"([^\"]*)\"$")
public void the_user_confirms_the_action_and_the_system_delete_phisically_the_task_with_check_on_db_for_the_delete_task_with_code(String code) throws Throwable {

	String checkDbAfterDelete="select * from REPLENISHMENT_TASK where code= '"+code+"'";
	SqlRowSet rowSet=db().template().queryForRowSet(checkDbAfterDelete);
	assertFalse(rowSet.next());
	
}
@After
public void deleteProcessing() throws Exception{
String code="test1";
valueMap.put("code", code);
String queryInsertPreProcessinig = TestDataUtils.filter(valueMap,"com/oneleo/goodsmovementsmanagement/api/test/slotReplenishmentService/changeStatus/deleteRepelnishment.sql");
db().template().execute(queryInsertPreProcessinig);
}

}
