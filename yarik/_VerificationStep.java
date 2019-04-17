package com.wba.rxdata.test;

import com.wba.test.utils.SoftAssert;
import com.wba.test.utils.VerificationUtils;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;

public class _VerificationStep extends _BaseStep {


    /**
     * The method allows to verify examples like this:<br>
     * 1 =  2 <br>
     * P.quantity == DB_QUANTITY<br>
     *
     * @param key1           -
     * @param comparisonSign -
     * @param key2           -
     */
    @Then("^verify that \"([^ ]+)\\s+([^ ]+)\\s+(.+)\"$")

    public void verify_that(String key1, String comparisonSign, String key2) {
        Object o1 = defineValue(key1);
        Object o2 = defineValue(key2);

        VerificationUtils.compare(o1, comparisonSign, o2);
    }

    /**
     * The method allows to verify examples like this:<br>
     * 1 + 2 = 2 <br>
     * P.quantity - C.quantity == DB_QUANTITY<br>
     *
     * @param example - String
     */
    @Then("^verify calculation: \"([^\"]*)\"$")
    public void verify_calculation(String example) {
        VerificationUtils.verify_example(example, "");
    }

    public void verifyDateTime(Object time1, String sign, Object time2) {
        VerificationUtils.compare(asMillSec(time1), sign, asMillSec(time2));
    }

    public void verifyDateEqualWithDelta(Object time1, Object time2, int delta) {
        final long actual = Math.abs(asMillSec(time1) - asMillSec(time2));
        assertTrue(String.format("Values is not in range %d milli sec : %d", delta, actual), actual <= delta);
    }

    @Then("fail if any errors")
    public void failIfAnyErrors() {
        new SoftAssert().failIfErrors();
    }

    @Given("debug step")
    public void debugStep() {
        LOGGER.debug("Debug");
    }

    void tmp(String... p) {
        LOGGER.debug(String.join(",", Arrays.stream(p).map(s->"'"+s+"'").collect(Collectors.toList())));
    }

}
