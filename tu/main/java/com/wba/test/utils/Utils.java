/* Copyright 2018 Walgreen Co. */
package com.wba.test.utils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Utils extends BaseStep {

    public static <T> boolean isEmpty(T... arrayOfAny) {
        return arrayOfAny == null || arrayOfAny.length == 0;
    }

    public static <T> T defaultOrFirst(T _default, T... arrayOfAny) {
        return isEmpty(arrayOfAny) ? _default : arrayOfAny[0];
    }

    public boolean isTagExist(String... tag) {
        return Arrays.stream(tag).allMatch(
                s -> asStr("~SCENARIO_TAGS").toLowerCase().matches(".*(?:^| )" + s.toLowerCase() + "(?: |$).*"));
    }

    public String getTagValue(String tag) {
        final List<String> tagValues = getTagValues(tag);
        return tagValues.size()>0 ? tagValues.get(0) : null;
    }

    public List<String> getTagValues(String tag) {
        return RegExp.getMatches(asStr("~SCENARIO_TAGS").toLowerCase(), "(?:^|\\s)" + tag + "([^ ]*)(?= |$)").stream()
                .map(l -> l[0])
                .collect(Collectors.toList());
    }
}
