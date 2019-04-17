/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils;

public class SnapshotTest extends BaseStep {

//    @Test
//    public void updateJsonDeleteItemTest() {
//        final String eventName = buildEvent();
//        final String json = eventStorage.getLastProduced().getBody();
//        Assert.assertEquals(new SnapshotUtils().getRowsInTable("T4", eventName + 0), Integer.valueOf(1));
//        final String s = new SnapshotUtils().updateSnapshot(json, "T4[0]", "delete::");
//        Assert.assertFalse(JsonUtils.isJsonPathExists(s, "T4[0]"));
//    }
//
//    @Test
//    public void getLastSnapshotNameTest() {
//        final String eventName = buildEvent();
//        buildEvent(eventName);
//        Assert.assertNull(eventStorage.getLastSnapshotName());
//        buildEvent(SnapShotBuilder.DEFAULT_SNAPSHOT_NAME);
//        Assert.assertEquals(eventName + 1, eventStorage.getLastSnapshotName(eventName));
//        Assert.assertEquals(SnapShotBuilder.DEFAULT_SNAPSHOT_NAME + 0, eventStorage.getLastSnapshotName());
//    }
//
//    @DataProvider(name = "data1")
//    public Object[][] data1() {
//        return new Object[][]{
//                {null, 0},
//                {"", 1},
//                {"sssss", 2}
//        };
//    }
//
//    @Test(dataProvider = "data1")
//    public void getRowByValueOKTest(String value, int rowNum) {
//        final String eventName = buildEvent();
//        final String rowByValue = new SnapshotUtils().getRowByValue("T3", "i2", value, eventName + 0);
//        Assert.assertEquals(rowByValue, String.format("%s.T3[%d].", eventName + 0, rowNum));
//    }
//
//    @Test(expectedExceptions = CucumberException.class,
//            expectedExceptionsMessageRegExp = "row is not found in table T3 for field i2 with value ssss")
//    public void getRowByValueFailTest() {
//        new SnapshotUtils().getRowByValue("T3", "i2", "ssss", buildEvent() + 0);
//    }
//
//    @Test
//    public void t01() {
//        final String eventName = buildEvent();
//        final List<String> rowsValue = new SnapshotUtils().getRowsValue("T3", "i2", eventName + 0);
//        Assert.assertEquals(rowsValue, Arrays.asList(null, "", "sssss"));
//    }
//
//    private String buildEvent(String... eventNameWithoutNumber) {
//        final String json = ResourceUtils.getResourceAsString("com/wba/test/data/snapshot.json");
//        final String eventName = Utils.defaultOrFirst(asStr("random::5"), eventNameWithoutNumber);
//        final String eventNameWithNumber = new SnapshotUtils().getNextEventName(eventName);
//        eventStorage.addProduced(new EventBuilder().name(eventNameWithNumber).body(json).build());
//        return eventName;
//    }

}
