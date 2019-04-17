/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/

package com.wba.test.utils.commonsteps;

import com.wba.test.utils.SoftAssert2;
import com.wba.test.utils.VerificationUtils;

import static org.junit.Assert.assertTrue;

public class VerificationStep extends BaseStep {

    /**
     * The method allows to verify examples like this:<br>
     * 1 =  2 <br>
     * P.quantity == DB_QUANTITY<br>
     *
     * @param key1           -
     * @param comparisonSign -
     * @param key2           -
     */
    public void verifyThat(String key1, String comparisonSign, String key2) {
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
    public void verifyCalculation(String example) {
        VerificationUtils.verify_example(example, "");
    }

    public void verifyDateTime(Object time1, String sign, Object time2) {
        VerificationUtils.compare(asMillSec(time1), sign, asMillSec(time2));
    }

    public void verifyDateEqualWithDelta(Object time1, Object time2, int delta) {
        final long actual = Math.abs(asMillSec(time1) - asMillSec(time2));
        assertTrue(String.format("Values is not in range %d milli sec : %d", delta, actual), actual <= delta);
    }

    public void failIfAnyErrors() {
        new SoftAssert2().failIfErrors();
    }
}

