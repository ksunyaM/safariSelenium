package com.oneleo.goodsmovementsmanagement.api.test.slotReplenishmentService.checkScannedStockUnit;

import static com.oneleo.test.automation.core.ApiUtils.api;
import static com.oneleo.test.automation.core.DatabaseUtils.db;
import static com.oneleo.test.automation.core.DatabaseUtils.jdbc;

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

public class CheckScannedStockUnitStep {

	String idStockGroup;
	String quantity;
	String idReplenishment;
	String quantityStarted;
	private SqlRowSet rowSet;
	Response val;
	Map<String, String> valueMap = new HashMap<String, String>();
	private ValidatableResponse response;
	String codeStockGroup;

	@Given("^There is at least one task with code \"([^\"]*)\" and status \"([^\"]*)\"$")
	public void there_is_at_least_one_task_with_code_and_status(String code, String status) throws Throwable {
		valueMap.put("code", code);
		String queryInsertPreProcessinig = TestDataUtils.filter(valueMap,
				"com/oneleo/goodsmovementsmanagement/api/test/slotReplenishmentService/checkScannedStockUnitFailure/insertReplenishmentTask.sql");
		jdbc().template().execute(queryInsertPreProcessinig);
		String check = "select QUANTITY from REPLENISHMENT_TASK where code='" + code + "'";
		SqlRowSet rowSet = jdbc().template().queryForRowSet(check);
		rowSet.next();
		quantityStarted = rowSet.getString(1);
	}

	@When("^the system selects a replenishment task with code \"([^\"]*)\" status \"([^\"]*)\" and warehouse \"([^\"]*)\"$")
	public void the_system_selects_a_replenishment_task_with_code_status_and_warehouse(String code, String status,
			String warehouse) throws Throwable {
		valueMap.put("status", status);
		valueMap.put("warehouse", warehouse);
		String jsonRequest = TestDataUtils.filter(valueMap,
				"com/oneleo/goodsmovementsmanagement/api/test/slotReplenishmentService/checkScannedStockUnit/read.json");
		response = api().template().contentType("application/json").body(jsonRequest)
				.post("goodsmovementsmanagement/rest/slotreplenishmenthht/readreplenishmenttasklist").then()
				.statusCode(200);
		String checkReplenishment = "select id,status,internal_code  from REPLENISHMENT_TASK where code='" + code + "'";
		SqlRowSet rowSet2 = db().template().queryForRowSet(checkReplenishment);
		rowSet2.next();
		idReplenishment = rowSet2.getString(1);
		String statusReplenishment = rowSet2.getString(2);
		String internalCodeReplenishment = rowSet2.getString(3);
		System.out.println(idReplenishment);
		System.out.println(statusReplenishment);
		System.out.println(internalCodeReplenishment);
		String checkReplenishmentLocation = "select source_location from REPLENISHMENT_LOCATION_LNK where id_replenishment_task='"
				+ idReplenishment + "'";
		SqlRowSet rowSet3 = db().template().queryForRowSet(checkReplenishmentLocation);
		rowSet3.next();
		String sourceLocation = rowSet3.getString(1);
		System.out.println(sourceLocation);
		// checkStock
		String checkStock = "select id from stock where internal_code='" + internalCodeReplenishment
				+ "' and warehouse_code='3701'";
		SqlRowSet rowSet4 = db().template().queryForRowSet(checkStock);
		rowSet4.next();
		String idStock = rowSet4.getString(1);
		System.out.println(idStock);

		String checkStockGroup = "select id,code from stock_group where id=(select max(id) from stock_group where id_stock='"
				+ idStock + "')";
		SqlRowSet rowSet5 = db().template().queryForRowSet(checkStockGroup);
		rowSet5.next();
		idStockGroup = rowSet5.getString(1);
		codeStockGroup = rowSet5.getString(2);
		System.out.println(idStockGroup);
		System.out.println(codeStockGroup);
		String checkStockItem = "select quantity from stock_item where id_stock_group='" + idStockGroup + "'";
		SqlRowSet rowSet6 = db().template().queryForRowSet(checkStockItem);
		rowSet6.next();
		quantity = rowSet6.getString(1);
		valueMap.put("sourceLocation", sourceLocation);
		valueMap.put("stockUnit", codeStockGroup);
		valueMap.put("code", code);
		valueMap.put("warehouse", warehouse);
		String jsonRequestCheck = TestDataUtils.filter(valueMap,
				"com/oneleo/goodsmovementsmanagement/api/test/slotReplenishmentService/checkScannedStockUnit/checkStockUnitForAddItem.json");
		System.out.println(jsonRequestCheck);
		response = api().template().contentType("application/json").body(jsonRequestCheck)
				.post("/goodsmovementsmanagement/rest/slotreplenishmenthht/additemtoreplenishmenttask").then()
				.statusCode(400);

	}

	@Then("^The system passes the replenishment tasks with code \"([^\"]*)\" and accepts and displays in the CountedQty field the stockUnit PBOH quantity$")
	public void the_system_passes_the_replenishment_tasks_with_code_and_accepts_and_displays_in_the_CountedQty_field_the_stockUnit_PBOH_quantity(
			String code) throws Throwable {

		String checkReplenishmentTask = "select EFFECTIVE_QUANTITY from replenishment_task where code='" + code + "'";
		SqlRowSet rowSet = db().template().queryForRowSet(checkReplenishmentTask);
		rowSet.next();
		String quantityReplenishment = rowSet.getString(1);
		System.out.println(quantityReplenishment);

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
