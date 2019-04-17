/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils;

import com.datastax.driver.core.utils.UUIDs;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.UUID;

public class ValueTemplateUtils {

    public static Object createValue(String value) {
        List<String[]> matches = RegExp.getMatches(value, "^(?:(\\w*)::)?(.*)");
        if (matches.isEmpty() || matches.get(0)[0] == null) {
            return value;
        }
        String format = matches.get(0)[0].toLowerCase();
        String parameter = matches.get(0)[1];

        if (StringUtils.isEmpty(parameter)) {
            parameter = "0";
        }
        switch (format) {
            case "default":
            case "def":
                return TemplateAction.DO_NOT_CHANGE;
            case "delete":
            case "deleted":
                return TemplateAction.DELETE;
            case "bd":
                return new BaseStep().asBigDec(parameter);
            case "l":
            case "long":
                return Long.parseLong(parameter);
            case "d":
            case "double":
                return Double.parseDouble(parameter);
            case "b":
            case "bool":
            case "boolean":
                return Boolean.parseBoolean(parameter);
            case "i":
            case "int":
                return Integer.parseInt(parameter);
            case "random":
                return RandomStringUtils.randomNumeric(Integer.parseInt(parameter));
            case "randomstring":
                return RandomStringUtils.randomAlphanumeric(Integer.parseInt(parameter));
            case "timestamp":
                return Long.parseLong(DateUtils.calculateDateTime(parameter, 3).toString());
            case "datetime":
            case "dt": // 2018-06-07 21:45:01.818Z
                return DateUtils.calculateDateTime(parameter, 0).toString();
            case "dtt":
            case "datetimet": // 2018-06-07T21:45:01.818Z
                return DateUtils.calculateDateTime(parameter, 1).toString();
            case "date": // 2018-06-07
                return DateUtils.calculateDateTime(parameter, 2).toString();
            case "days": // 17702 (days from epoch)
                return DateUtils.calculateDateTime(parameter, 4);
            case "uuid":
            case "random_uuid":
                return UUID.randomUUID().toString();
            case "timeuuid":
                return UUIDs.timeBased().toString();
            case "null":
                return null;
            default:
                return format + RandomStringUtils.randomNumeric(Integer.parseInt(parameter));
        }
    }

    public enum TemplateAction {
        DELETE(), DO_NOT_CHANGE()
    }

}
