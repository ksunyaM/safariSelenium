/*
 * Copyright 2018 Walgreen Co.
 */

package com.wba.test.utils.mocks;

class DataMock {

    private String serverURL;
    private String requestJson;
    private String name;

    private boolean overwrite;
    private Integer mockId;

    DataMock(String serverURL, String requestJson, String name, boolean overwrite) {
        this.serverURL = serverURL;
        this.requestJson = requestJson;
        this.name = name;
        this.overwrite = overwrite;
    }

    String getServerURL() {
        return serverURL;
    }

    void setServerURL(String serverURL) {
        this.serverURL = serverURL;
    }

    String getRequestJson() {
        return requestJson;
    }

    public String getName() {
        return name;
    }

    boolean isOverwrite() {
        return overwrite;
    }

    Integer getMockId() {
        return mockId;
    }

    void setMockId(int mockId) {
        this.mockId = mockId;
    }
}
