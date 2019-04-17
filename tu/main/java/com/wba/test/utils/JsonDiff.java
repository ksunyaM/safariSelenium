/*******************************************************************************
 * Copyright 2018 Walgreen Co.
 ******************************************************************************/
package com.wba.test.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class JsonDiff {

    private List<String> items = new ArrayList<>();
    private boolean useDotNotation = true;
    private boolean useRegExp = false;

    public String verify(String diff) {
        List<String> notMatch = new ArrayList<>();
        for (String item : items) {
            String prev = diff;
            diff = diff.replaceFirst(item, "");
            if (prev.length() == diff.length()) {
                notMatch.add(item);
            }
        }
        diff = RegExp.replaceAll(diff, "    ,\n", "");
        diff = RegExp.replaceAll(diff, "\\[\n    \n\\]", "[]");

        return diff + notMatch.stream().collect(Collectors.joining(",\n"));
    }

    public JsonDiff useDotNotation(boolean useDotNotation) {
        this.useDotNotation = useDotNotation;
        return this;
    }

    public JsonDiff useRegExp(boolean useRegExp) {
        this.useRegExp = useRegExp;
        return this;
    }

    public JsonDiff add(String key, Object value) {
        items.add(String.format("\\Q{\n" +
                "        \"op\": \"add\",\n" +
                "        \"path\": \"\\E%s\\Q\",\n" +
                "        \"value\": \\E%s\\Q\n" +
                "    }\\E", setPath(key), setValue(value)));
//        items.add(String.format("{\n    \"op\":\"add\",\n    \"path\":\"%s\",\n    \"value\":%s\n    }", setPath(key), setValue(value)));
        return this;
    }

    public JsonDiff replace(String key, Object value) {
        items.add(String.format("\\Q{\n" +
                "        \"op\": \"replace\",\n" +
                "        \"path\": \"\\E%s\\Q\",\n" +
                "        \"value\": \\E%s\\Q\n" +
                "    }\\E", setPath(key), setValue(value)));
//        items.add(String.format("{\n    \"op\":\"replace\",\n    \"path\":\"%s\",\n    \"value\":%s\n    }", setPath(key), setValue(value)));
        return this;
    }

    public JsonDiff remove(String key) {
        items.add(String.format("\\Q{\n" +
                "        \"op\": \"remove\",\n" +
                "        \"path\": \"\\E%s\\Q\"\n" +
                "    }\\E", setPath(key)));
//        items.add(String.format("{\n    \"op\":\"remove\",\n    \"path\":\"%s\"\n    }", setPath(key)));
        return this;
    }

    private Object setValue(Object value) {
        String f = value instanceof String ? "\"%s\"" : "%s";
        value = useRegExp ? value : Pattern.quote(value.toString());
        return value instanceof String ? String.format(f, value) : value;
    }

    private String setPath(String path) {
        return useDotNotation ?
                Pattern.quote("/" + Arrays.stream(path.split("\\.")).map(s -> {
                    final String[] m = RegExp.getMatches(s, "([^\\[]+)(?:\\[(\\d+)\\])?$").get(0);
                    return m[0] + (m[1] == null ? "" : "/" + m[1]);
                }).collect(Collectors.joining("/"))) :
                useRegExp ? path : Pattern.quote(path);
    }

}
