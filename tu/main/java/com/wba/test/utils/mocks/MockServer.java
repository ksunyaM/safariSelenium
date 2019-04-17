/*
 * Copyright 2018 Walgreen Co.
 */

package com.wba.test.utils.mocks;

public enum MockServer implements IMockServer {

    RXP("{apiserver.uri}/rxpmock/virtualservices"),
    CS("{apiserver.uri}/csmock/virtualservices"),
    POM("{apiserver.uri}/pommock/virtualservices"),
    CUSTOM("");

    private String url;

    MockServer(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public MockServer setUrl(String url) {
        this.url = url;
        return this;
    }
}
