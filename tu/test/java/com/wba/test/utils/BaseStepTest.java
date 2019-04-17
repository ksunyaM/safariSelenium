/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils;

import com.wba.test.utils.kafka.EventBuilder;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class BaseStepTest extends BaseStep {

    @BeforeClass
    public void data() {
        dataStorage.add("t1", 123);
        dataStorage.add("t2", "123");
        dataStorage.add("t3", "abc");
        dataStorage.add("t4", "2018-03-20 12:31:37.200Z");
        dataStorage.add("t5", null);
        dataStorage.add("t6", "1523713573955");
        dataStorage.add("t7", "17702");
        dataStorage.add("t8", "123.01");
        dataStorage.add("t9", 123.01);
        dataStorage.add("t10", 123.000011);
        dataStorage.add("t11", 123.000016);
        eventStorage.addProduced(
                new EventBuilder().name("Q").body("{\"a\":[ {\"q\": 1, \"a\":11}, {\"q\": 2, \"a\":21}]}").build());
        eventStorage.addProduced(
                new EventBuilder()
                        .body("{\"int\":1, \"double\":2.0, \"cur\": 1.12, \"text\":\"text\", \"null\":null, \"bool\": false, \"array\": [{\"a\":1},{\"a\":2}]}")
                        .build());
    }

    @Test
    public void asBigDecTest() {
        Assert.assertEquals(asBigDec("t1").toString(), "123.00000");
        Assert.assertEquals(asBigDec("t2").toString(), "123.00000");
        Assert.assertEquals(asBigDec("t8").toString(), "123.01000");
        Assert.assertEquals(asBigDec("t9").toString(), "123.01000");
        Assert.assertEquals(asBigDec("t10").toString(), "123.00001");
        Assert.assertEquals(asBigDec("t11").toString(), "123.00002");
        Assert.assertEquals(asBigDec("0.05").toString(), "0.05000");
    }

    @Test
    public void asBigDecTest01() {
        Assert.assertEquals(asStr("bd::4"), "4.00000");
        Assert.assertEquals(asStr("bd::4.005"), "4.00500");
        Assert.assertEquals(asStr("bd::4.005003"), "4.00500");
    }

    @Test
    public void asLongTest() {
        Assert.assertEquals(asLong("t1"), Long.valueOf(123));
        Assert.assertEquals(asLong("t2"), Long.valueOf(123));
    }

    @Test
    public void asIntTest() {
        Assert.assertEquals(asInt("t1"), Integer.valueOf(123));
        Assert.assertEquals(asInt("t2"), Integer.valueOf(123));
        Assert.assertEquals(asInt("P.int"), Integer.valueOf(1));
        Assert.assertEquals(asInt("P..int"), Integer.valueOf(1));
        Assert.assertEquals(asInt("P.double"), Integer.valueOf(2));
    }

    @Test
    public void asStringTest() {
        Assert.assertEquals(asStr("t3"), "abc");
        Assert.assertNull(asStr("t5"));
        Assert.assertEquals(asStr("P..int"), "1");
    }

    @Test
    public void asMillSecTest() {
        Assert.assertEquals(asMillSec("2018-10-11").toString(), "1539216000000");
        Assert.assertEquals(asMillSec("t4").toString(), "1521549097200");
        Assert.assertEquals(asMillSec("t6").toString(), "1523713573955");
        Assert.assertEquals(asMillSec("t7").toString(), "1529452800000");
    }

    @Test
    public void nullTest() {
        Assert.assertNull(asBigDec(null));
        Assert.assertNull(asBigDec("P..null"));
        Assert.assertNull(asBigDec("t5"));
        Assert.assertNull(asMillSec("t5"));
        Assert.assertNull(asInt("t5"));
        Assert.assertNull(asLong("t5"));
        Assert.assertNull(asStr("t5"));
    }

    @Test
    public void asListTest() {
        Assert.assertEquals(asList("Q.$..q").get(1).asInt(), 2);
    }
}
