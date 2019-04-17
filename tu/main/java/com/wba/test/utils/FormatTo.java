/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils;

public enum FormatTo {
    LOG("yyyyMMddHHmmssSSS"),
    CAS_DT("yyyy-MM-dd HH:mm:ss.SSSz"),
    CAS_DT_T("yyyy-MM-dd'T'HH:mm:ss.SSSz"),
    DATETIME01("EEE MMM dd HH:mm:ss zzz yyy") // Thu Jun 07 15:30:18 CDT 2018
    ;

    private String format;

    FormatTo(String format) {
        this.format = format;
    }

    public String getFormat() {
        return format;
    }
}
