package com.oneleo.goodsmovementsmanagement.api.test.slotReplenishmentService.searchSlot;

import static com.oneleo.test.automation.core.ApiUtils.api;
import static com.oneleo.test.automation.core.DatabaseUtils.db;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.oneleo.test.automation.core.TestDataUtils;

import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.ValidatableResponse;

public class SearchSlotStep {
	
	Map<String, String> valueMap = new HashMap<String, String>();
	String requestAsString;
	private ValidatableResponse responseService;
	private String responseServiceAsString;
	private SqlRowSet rowSet,rowSet2;
	private ValidatableResponse response=null;
	
	@Given("^A Replenishment Task exists matching following criteria with \"([^\"]*)\",\"([^\"]*)\",\"([^\"]*)\",\"([^\"]*)\" and \"([^\"]*)\"$")
	public void a_Replenishment_Task_exists_matching_following_criteria_with_and(String code, String internalCode, String status, String warehouseCode, String type) throws Throwable {
		String checkDb = "select * from REPLENISHMENT_TASK where code='"+code+"' and INTERNAL_CODE='"+internalCode+"'and status='"+status+"' and warehouse_code='"+warehouseCode+"'and type='"+type+"'";
		rowSet=db().template().queryForRowSet(checkDb);
		rowSet.next();
	}

	@When("^The system searches a Replenishment Task with following criteria with \"([^\"]*)\" and \"([^\"]*)\"$")
	public void the_system_searches_a_Replenishment_Task_with_following_criteria_with_and(String status, String warehouseCode) throws Throwable {
		valueMap.put("status",status);	
		valueMap.put("warehouseCode",warehouseCode);

		String jsonRequest=TestDataUtils.filter(valueMap,"com/oneleo/goodsmovementsmanagement/api/test/slotReplenishmentService/searchSlot/readSlot.json");
		response = api().template().
				contentType("application/json").
				body(jsonRequest).
				post("/goodsmovementsmanagement/rest/slotreplenishmenthht/readreplenishmenttasklist").
				then().statusCode(200);
	}

	@Then("^The System retrives the searched slot with the following \"([^\"]*)\" and stock \"([^\"]*)\"$")
	public void the_System_retrives_the_searched_slot_with_the_following_and_stock(String codeSlot, String codeStock) throws Throwable {
		String checkStockGroup = "select id from STOCK_GROUP where code='"+codeStock+"'";
		rowSet = db().template().queryForRowSet(checkStockGroup);
		rowSet.next();
		String idStockGroup = rowSet.getString(1);
		//System.out.println(id_stock_Group);
		String checkDb = "select slot_code from  stock_group_slot_link where ID_STOCK_GROUP='"+idStockGroup+"'";
		rowSet2 = db().template().queryForRowSet(checkDb);
		rowSet2.next();
		String codeSlotCode= rowSet2.getString(1);
		//System.out.println(codeStrockGroup);
        Assert.assertEquals(codeSlotCode,codeSlot);
		
		
	}
	@After
	public void deleteProcessing() throws Exception{
	String code="test1";
	valueMap.put("code", code);
	String querydeletePreProcessinig = TestDataUtils.filter(valueMap,"com/oneleo/goodsmovementsmanagement/api/test/slotReplenishmentService/changeStatus/deleteRepelnishment.sql");
	db().template().execute(querydeletePreProcessinig);
	}


}
