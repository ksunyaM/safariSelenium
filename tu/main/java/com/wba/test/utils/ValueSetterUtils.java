/*
 * Copyright 2019 Walgreen Co.
 */

package com.wba.test.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ValueSetterUtils {

    public static String jsonSetValues(String json) {
        if (null == json) return null;

        final BaseStep bs = new BaseStep();

        HashMap<String, List<List<String>>> data = new HashMap<>();
        HashMap<String, String> values = new HashMap<>();

        final String noData = bs.asStr("uuid::");
        // Collect Data
        RegExp.getMatches(json, "(\"\\$[\\$!]([\\w_]+)(?:\\:([^\"\n\r]+))?\")").forEach(l -> {
            // 0 - replace, 1 - variable, 2 - value
            final String value = l[2] == null ? noData : bs.asStr(l[2]);
            if (l[2] != null) values.putIfAbsent(l[1], value);
            data.putIfAbsent(l[1], new ArrayList<>());
            data.get(l[1]).add(Arrays.asList(l[0], value));
        });

        // set value for all empty set
        for (String var : data.keySet()) {
            for (List<String> replacement : data.get(var)) {
                List<String> quotes = replacement.get(0).contains("$$") ? Arrays.asList("\"", "\"") : Arrays.asList("", "");
                json = json.replaceFirst(
                        "\\Q" + replacement.get(0) + "\\E",
                        quotes.get(0) + (replacement.get(1).equals(noData) ? values.get(var) : replacement.get(1)) + quotes.get(1));
//                LOGGER.trace(replacement.toString());
            }
        }
        return JsonUtils.prettify(json);
    }
}
