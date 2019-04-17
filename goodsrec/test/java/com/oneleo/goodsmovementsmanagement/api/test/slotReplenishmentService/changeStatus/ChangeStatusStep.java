package com.oneleo.goodsmovementsmanagement.api.test.slotReplenishmentService.changeStatus;

import static com.oneleo.test.automation.core.ApiUtils.api;
import org.junit.Assert;
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

public class ChangeStatusStep {

	private SqlRowSet rowSet;
	Response val;
	Map<String, String> valueMap = new HashMap<String, String>();
	private ValidatableResponse response;

	@Before
	public void insertBeforeProcess() throws Throwable {

		String code = "test1";
		valueMap.put("code", code);
		String queryInsertPreProcessinig = TestDataUtils.filter(valueMap,
				"com/oneleo/goodsmovementsmanagement/api/test/slotReplenishmentService/checkScannedStockUnitFailure/insertReplenishmentTask.sql");
		db().template().execute(queryInsertPreProcessinig);
		String update = "UPDATE REPLENISHMENT_TASK SET Status = 'Open' where code='" + code + "'";
		db().template().update(update);
		String check = "select *  from REPLENISHMENT_TASK where code='" + code + "'";
		SqlRowSet rowSet = db().template().queryForRowSet(check);
		rowSet.next();
	}

	@Given("^There is at least one task with code \"([^\"]*)\" and status \"([^\"]*)\"$")
	public void there_is_at_least_one_task_with_code_and_status(String code, String status) throws Throwable {

		String check = "select status  from REPLENISHMENT_TASK where code='" + code + "'";
		SqlRowSet rowSet2 = db().template().queryForRowSet(check);
		rowSet2.next();

	}

	@When("^the system selects a replenishment task with code \"([^\"]*)\" status \"([^\"]*)\" and warehouse \"([^\"]*)\"$")
	public void the_system_selects_a_replenishment_task_with_code_status_and_warehouse(String code, String changeStatus,
			String warehouse) throws Throwable {

		valueMap.put("code", code);
		valueMap.put("warehouse", warehouse);
		String jsonRequest = TestDataUtils.filter(valueMap,
				"com/oneleo/goodsmovementsmanagement/api/test/slotReplenishmentService/changeStatus/claim.json");
		response = api().template().contentType("application/json").body(jsonRequest)
				.post("goodsmovementsmanagement/rest/slotreplenishmenthht/claimreplenishmenttask").then()
				.statusCode(200);
		String check = "select status  from REPLENISHMENT_TASK where code='" + code + "'";
		SqlRowSet rowSet2 = db().template().queryForRowSet(check);
		rowSet2.next();
		String statusStarted = rowSet2.getString(1);
		//System.out.println(statusStarted);
		Assert.assertEquals(statusStarted, changeStatus);
		valueMap.put("code", code);
		valueMap.put("warehouse", warehouse);
		String jsonRequestConfirm = TestDataUtils.filter(valueMap,
				"com/oneleo/goodsmovementsmanagement/api/test/slotReplenishmentService/changeStatus/confirm.json");
		response = api().template().contentType("application/json").body(jsonRequestConfirm)
				.post("goodsmovementsmanagement/rest/slotreplenishmenthht/confirmreplenishmenttask").then()
				.statusCode(200);

	}

	@Then("^The system passes the replenishment tasks with code \"([^\"]*)\"  and statusfinal \"([^\"]*)\" from Started to Pending$")
	public void the_system_passes_the_replenishment_tasks_with_code_and_statusfinal_from_Started_to_Pending(String code,
			String statusfinal) throws Throwable {

		String checkToDb = "select status from REPLENISHMENT_TASK where code='" + code + "'";
		SqlRowSet rowSet3 = db().template().queryForRowSet(checkToDb);
		rowSet3.next();
		String statusPending = rowSet3.getString(1);
	//	System.out.println(statusPending);
		Assert.assertEquals(statusPending, statusfinal);

	}

	@After
	public void deleteProcessing() throws Exception {

		String code = "test1";
		valueMap.put("code", code);
		String querydeletePreProcessinig = TestDataUtils.filter(valueMap,
				"com/oneleo/goodsmovementsmanagement/api/test/slotReplenishmentService/changeStatus/deleteRepelnishment.sql");
		db().template().execute(querydeletePreProcessinig);
	
	}

}
