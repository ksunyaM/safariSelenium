/* Copyright 2018 Walgreen Co. */
package com.wba.test.utils;

import com.wba.test.utils.kafka.EventBuilder;
import com.wba.test.utils.mocks.MockBuilder;
import com.wba.test.utils.mocks.MockServer;
import com.wba.test.utils.mocks.MockUtils;
import org.testng.annotations.AfterTest;

@SuppressWarnings("unused")
public class MockTest extends BaseStep {

    private static final String PATH_TO_DATA = "com/wba/test/data/";

    //    @Test
    public void t01() {
        MockServer.CUSTOM.setUrl("qwe");
        LOGGER.warn(MockServer.CUSTOM.getUrl());
    }

    //    @Test
    public void tCreateMock01() {

        new MockBuilder()
                .name("Mock1")
//                .operationId("readPlanByCode")
                .mockServer(MockServer.RXP)
                .httpStatusCode("200")
                .method("POST")
                .url("/rxplan/{planCode}")
                .output("{}")
                .addParam("planCode", asStr("random::8"))
                .addHeader("correlationId", asStr("random::8"))
//                .addParam("planCode", "test01")
                .build();
    }

    //    @Test
    public void mockTest2() {

        new MockBuilder()
                .httpStatusCode("200")
                .method("GET")
                .url("/locations/{locationType}/{locationNumber}")
//                .operationId("readLocationByBusinessKey")
                .output(ResourceUtils.getResourceAsString(PATH_TO_DATA + "locationOutput.json"))
                .addParam("locationNumber", asStr("random::8"))
                .addParam("locationType", "Store")
                .build();
    }

    //    @Test
    public void mockTest3() {
        new MockBuilder(
                "/products/bulk_list",
                "bulkList",
                "{\n  \"actualProductPackCodes\" : [\n\"1\", \"3\"\n]\n}",
                "{}",
                null,
                "POST",
                "200")
                .addParam("expand", "")
                .addParam("fields", null)
                .build();
    }

    //    @Test
    public void prepare_purchasing_mocks_for_and_with() {
        new MockBuilder()
                .httpStatusCode("200")
                .method("GET")
                .url("/processedpurchaseorderlines?locationNumber={locationNumber}&locationType={locationType}&app_code={app_code}&upc={upc}")
//                .operationId("readProcessedPurchaseOrderLineList")
                .output("{}")
                .addParam("locationNumber", asStr("random::8"))
                .addParam("locationType", "Pharmacy")
                .addParam("app_code", "tmp" + asStr("random::8"))
                .addParam("upc", "tmp" + asStr("random::8"))
                .build();

        eventStorage.addProduced(new EventBuilder().name("Mock01").body("{}").build());
    }


    @AfterTest
    public void deleteMocks() {
        new MockUtils().deleteMocks();
    }

}
