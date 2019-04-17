package com.oneleo.goodsmovementsmanagement.api.test.slotReplenishmentService.checkScannedStockUnitFailure;

import static com.oneleo.test.automation.core.ApiUtils.api;
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

public class CheckScannedStockUnitFailureStep {
	private String idStockGroup;
	private String quantity;
	private String idReplenishment;
	private String quantityStarted;
	private SqlRowSet rowSet;
	private Response val;
	private Map<String, String> valueMap = new HashMap<String, String>();
	private ValidatableResponse response;
	private String codeStockGroup;

	@Given("^There is at least one task with code \"([^\"]*)\" and status \"([^\"]*)\"$")
	public void there_is_at_least_one_task_with_code_and_status(String code, String status) throws Throwable {
		valueMap.put("code", code);
		String query = TestDataUtils.filter(valueMap,
				"com/oneleo/goodsmovementsmanagement/api/test/slotReplenishmentService/checkScannedStockUnitFailure/deleteRepelnishment.sql");
		jdbc().template().execute(query);
		String queryInsertPreProcessinig = TestDataUtils.filter(valueMap,
				"com/oneleo/goodsmovementsmanagement/api/test/slotReplenishmentService/checkScannedStockUnitFailure/insertReplenishmentTask.sql");
		jdbc().template().execute(queryInsertPreProcessinig);
		String check = "select QUANTITY from REPLENISHMENT_TASK where code='" + code + "'";
		SqlRowSet rowSet = jdbc().template().queryForRowSet(check);
		rowSet.next();
		quantityStarted = rowSet.getString(1);
	}

	@When("^the system selects a replenishment task with code \"([^\"]*)\" status \"([^\"]*)\" and warehouse \"([^\"]*)\" and stockgroup \"([^\"]*)\"$")
	public void the_system_selects_a_replenishment_task_with_code_status_and_warehouse_and_stockgroup(String code,
			String status, String warehouse, String stockgroup) throws Throwable {

		valueMap.put("status", status);
		valueMap.put("warehouse", warehouse);
		String jsonRequest = TestDataUtils.filter(valueMap,
				"com/oneleo/goodsmovementsmanagement/api/test/slotReplenishmentService/checkScannedStockUnit/read.json");
		response = api().template().contentType("application/json").body(jsonRequest)
				.post("goodsmovementsmanagement/rest/slotreplenishmenthht/readreplenishmenttasklist").then()
				.statusCode(200);
		String checkReplenishment = "select id,status,internal_code  from REPLENISHMENT_TASK where code='" + code + "'";
		SqlRowSet rowSet2 = jdbc().template().queryForRowSet(checkReplenishment);
		rowSet2.next();
		idReplenishment = rowSet2.getString(1);
		String statusReplenishment = rowSet2.getString(2);
		String internalCodeReplenishment = rowSet2.getString(3);
		String checkReplenishmentLocation = "select source_location from REPLENISHMENT_LOCATION_LNK where id_replenishment_task='"
				+ idReplenishment + "'";
		SqlRowSet rowSet3 = jdbc().template().queryForRowSet(checkReplenishmentLocation);
		rowSet3.next();
		String sourceLocation = rowSet3.getString(1);
		// checkStock
		String checkStock = "select id from stock where internal_code='" + internalCodeReplenishment
				+ "' and warehouse_code='3701'";
		SqlRowSet rowSet4 = jdbc().template().queryForRowSet(checkStock);
		rowSet4.next();
		String idStock = rowSet4.getString(1);

		String checkStockGroup = "select id,code from stock_group where id=(select id from stock_group where id_stock='"
				+ idStock + "' and reserved = 0)";
		SqlRowSet rowSet5 = jdbc().template().queryForRowSet(checkStockGroup);
		rowSet5.next();
		idStockGroup = rowSet5.getString(1);
		// codeStockGroup=rowSet5.getString(2);
		String checkStockItem = "select quantity from stock_item where id_stock_group='" + idStockGroup + "'";
		SqlRowSet rowSet6 = jdbc().template().queryForRowSet(checkStockItem);
		rowSet6.next();
		quantity = rowSet6.getString(1);
		valueMap.put("sourceLocation", sourceLocation);
		valueMap.put("stockUnit", stockgroup);
		valueMap.put("code", code);
		valueMap.put("warehouse", warehouse);
		String jsonRequestCheck = TestDataUtils.filter(valueMap,
				"com/oneleo/goodsmovementsmanagement/api/test/slotReplenishmentService/checkScannedStockUnitFailure/checkstockunirforadditem.json");
		System.out.println(jsonRequestCheck);
		response = api().template().contentType("application/json").body(jsonRequestCheck)
				.post("/goodsmovementsmanagement/rest/slotreplenishmenthht/additemtoreplenishmenttask").then()
				.statusCode(400);
		System.out.println(response);
	}

	@Then("^The system displays a blocking message$")
	public void the_system_displays_a_blocking_message() throws Throwable {

		String code = "00100";
		valueMap.put("code", code);
		String check = "select EFFECTIVE_QUANTITY from REPLENISHMENT_TASK where code='" + code + "'";
		SqlRowSet rowSet = jdbc().template().queryForRowSet(check);
		rowSet.next();
		quantityStarted = rowSet.getString(1);

	}

	@After
	public void cleanIWTO() throws Exception {

		String query = TestDataUtils.filter(valueMap,
				"com/oneleo/goodsmovementsmanagement/api/test/slotReplenishmentService/checkScannedStockUnitFailure/deleteRepelnishment.sql");
		jdbc().template().execute(query);
	}
}
