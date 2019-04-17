/* Copyright 2018 Walgreen Co. */
package com.wba.test.utils;

/**
 * Cucumber scenario annotations for test forkflow modification e.g. @noprettify.
 */
@SuppressWarnings("SpellCheckingInspection")
public enum ScenarioTag {
    NOPRETTIFY,
    CTIMEOUT;

    public String get() {
        return "@" + this.name();
    }
}
