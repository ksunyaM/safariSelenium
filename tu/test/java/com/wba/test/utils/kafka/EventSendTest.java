/*
 * Copyright 2019 Walgreen Co.
 */

package com.wba.test.utils.kafka;

import com.wba.commonsteps._BaseStep;
import com.wba.commonsteps._CommonStep;
import com.wba.test.utils.SoftAssert2;
import org.testng.annotations.BeforeClass;

import java.util.UUID;

public class EventSendTest extends _BaseStep {
    //        final String p = ResourceUtils.getResourceAsString("com/wba/test/data/prescriber_load.json");

    @BeforeClass
    public void createData() {
        dataStorage.add("uniqueID", UUID.randomUUID().toString());
        dataStorage.add("~~startTime", asMillSec("timestamp::"));

        System.setProperty("javax.net.ssl.trustStore", "/opt/wagapp/key_store/umtruststore.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "changeit");

        System.setProperty("java.security.auth.login.config", "/opt/wagapp/key_store/kafka_client_jaas.conf");
        System.setProperty("java.security.krb5.conf", "/opt/wagapp/key_store/krb5.conf");
        System.setProperty("javax.security.auth.useSubjectCredsOnly", "true");

//        String fileName = "conf/system.properties";
//        try {
//            Properties properties = System.getProperties();
//            properties.load(getClass().getClassLoader().getResourceAsStream(fileName));
//            System.setProperties(properties);
//        } catch (IOException ignored) {
//        }
    }

    // TODO: tests required environment configuration to be executed on jenkins
//    @Test
//    public void tEventWithKey() {
//        produceEvent("test01", "dev-rxprescriber_pbrplan_createrequest");
//        new SoftAssert2()
//                .equals(asStr("C::key.contextId"), asStr("uniqueID"))
//                .equals(asStr("P::key.contextId"), asStr("uniqueID"))
//                .equals(asStr("test01::key.contextId"), asStr("uniqueID"))
//                .equals(asStr("C::key.contextId1"), "C::key.contextId1")
//                .failIfErrors();
//        LOGGER.trace("ok");
//    }

    //    @Test
    public void tEventWithKey01() {
        _CommonStep cs = new _CommonStep();
        cs.a_event_for_topic("test01", "dev-rxprescriber_pbrplan_createrequest");
        updateEventKey("contextId", "qwe");
        updateEventKey("contextId1", "uniqueID");
        new SoftAssert2()
                .equals(asStr("P::key.contextId1"), asStr("uniqueID"))
                .failIfErrors();
    }

    //    @Test
    public void tEventNoKey() {
        produceEvent("test02", "rxprescriber_updaterequest");
        LOGGER.trace("ok");
    }

    private void produceEvent(String eventName, String topicName) {
        _CommonStep cs = new _CommonStep();
        cs.a_event_for_topic(eventName, topicName);
        cs.the_event_is_produced_by_test(eventName);
        cs.a_new_event_is_produced_to_topic(eventName + "_C", topicName);
    }
}
