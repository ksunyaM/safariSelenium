/* Copyright 2018 Walgreen Co. */
package com.wba.commonsteps;

import com.wba.test.utils.commonsteps.VerificationStep;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

public class _VerificationStep extends _BaseStep {

    private VerificationStep verificationStep = new VerificationStep();

    @Then("^verify that \"([^ ]+)\\s+([^ ]+)\\s+(.+)\"$")
    public void verifyThat(String key1, String comparisonSign, String key2) {
        verificationStep.verifyThat(key1, comparisonSign, key2);
    }

    @Then("^verify calculation: \"([^\"]*)\"$")
    public void verifyCalculation(String example) {
        verificationStep.verifyCalculation(example);
    }

    public void verifyDateTime(Object time1, String sign, Object time2) {
        verificationStep.verifyDateTime(time1, sign, time2);
    }

    public void verifyDateEqualWithDelta(Object time1, Object time2, int delta) {
        verificationStep.verifyDateEqualWithDelta(time1, time2, delta);
    }

    @Then("fail if any errors")
    public void failIfAnyErrors() {
        verificationStep.failIfAnyErrors();
    }

    @Given("debug step")
    public void debugStep() {
        LOGGER.debug("Debug");
    }
}