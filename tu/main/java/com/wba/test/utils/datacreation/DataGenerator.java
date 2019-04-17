/* Copyright 2018 Walgreen Co. */
package com.wba.test.utils.datacreation;

import com.wba.test.utils.BaseStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class DataGenerator extends BaseStep {
    private static Logger LOGGER = LoggerFactory.getLogger(DataGenerator.class);
    private List simpleTypes = Arrays.asList("string", "int", "long", "double");
    private String schemaType;
    private Map<String, List<String>> fieldsConfigMap = new Fields().get().getFields();
    private Function<String, String> dataTransformation = (s) -> "#" + s;
    private DataTypeProvider rules = new DataTypeProvider();

    public DataGenerator(Function<String, String> dataTransformation){
        this.dataTransformation = dataTransformation;
    }

    public DataGenerator(){}

    public void setSchemaType(String schemaType){
        this.schemaType = schemaType;
    }

    public void setRulesProvider(DataTypeProvider rules){
        this.rules = rules;
    }

    public Object generateValue(String path, String value) {
        String field = path;
        String[] pathEl = field.split("\\.");
        if (simpleTypes.contains(pathEl[pathEl.length - 1])) field = path.substring(0, path.lastIndexOf("."));
        for (String k : fieldsConfigMap.keySet()) {
            if (fieldsConfigMap.get(k).contains(field)) return dataTransformation.apply(k);
        }
        if (pathEl.length > 1) return generateValue(field.substring(field.indexOf(".") + 1), value);
        return defineValue(rules.applyDataType(field, value, schemaType));
    }

    public void applyData(Function<String, String> dataTransformation){
        this.dataTransformation = dataTransformation;
    }
}
