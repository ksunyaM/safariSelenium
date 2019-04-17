/* Copyright 2018 Walgreen Co. */
package com.wba.test.utils.datacreation;

import com.wba.test.utils.DataStorage;
import com.wba.test.utils.ValueTemplateUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.math3.random.RandomDataGenerator;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@SuppressWarnings("unchecked")
public class DataTypeProvider {
    private String ISO = "USD";
    private String symbol = "$";
    protected String value;
    private RandomDataGenerator random = new RandomDataGenerator();
    private DataStorage dataStorage = DataStorage.getInstance();
    private Map<String, Function<String, String>> dataTypes = new HashMap<>();

    public DataTypeProvider(){
        addDataType("Currency", this::applyCurrencyDataType);
        addDataType("Name", this::applyNameDataType);
        addDataType("LocationIdentifier", this::applyLocationDataType);
    }

    protected void addDataType(String dataTypeName, Function<String, String> function){
        dataTypes.put(dataTypeName, function);
    }

    protected String applyDataType(String fieldPath, String value, String type){
        if(!dataStorage.map().keySet().contains("generatedParams"))dataStorage.add("generatedParams", new HashMap<>());
        this.value = value;
        return dataTypes.keySet().contains(type)? dataTypes.get(type).apply(fieldPath):value;
    }

    protected String applyCurrencyDataType(String fieldPath){
        Map<String, String> currencyParams = (Map)dataStorage.map().get("generatedParams");
        switch (fieldPath){
            case "amount":{
                currencyParams.put("amount", new DecimalFormat("##.##").format(random.nextUniform(1D, 100D)));
                dataStorage.map().put("generatedParams", currencyParams);
                return currencyParams.get("amount");
            }
            case "isoCode": return ISO;
            case "symbol": return symbol;
            case "decimalValue": return "d::" + currencyParams.get("amount");
            case "formattedAmount": return symbol + currencyParams.get("amount");
            default: return value;
        }
    }

    protected String applyNameDataType(String fieldPath){
        String[] firstNames = {"John", "Mary", "Liam", "Emily", "William", "Hannah"};
        String[] lastNames = {"Smith", "Johnson", "Allen", "Coleman", "Simmons", "Carter"};
        Map<String, String> nameParams = (Map)dataStorage.map().get("generatedParams");
        switch (fieldPath){
            case "firstName":{
                nameParams.put("firstName", firstNames[random.nextInt(0, firstNames.length - 1)]);
                dataStorage.map().put("generatedParams", nameParams);
                return nameParams.get("firstName");
            }
            case "lastName": {
                nameParams.put("lastName", lastNames[random.nextInt(0, lastNames.length - 1)]);
                dataStorage.map().put("generatedParams", nameParams);
                return nameParams.get("lastName");
            }
            case "initials": return String.valueOf(nameParams.get("firstName").charAt(0)) + String.valueOf(nameParams.get("lastName").charAt(0));
            case "middleNameInitial": return String.valueOf(nameParams.get("firstName").charAt(0));
            default: return value;
        }
    }

    protected String applyLocationDataType(String fieldPath){
        String env = System.getProperty("confPath") == null ? "dev" : StringUtils.substringAfter(System.getProperty("confPath"), "conf").substring(1);
        String locationNumber = env.equals("dev") || env.equals("devqe") ? "59511"
                : env.equals("sit") || env.equals("e2e") ? "59345" : ValueTemplateUtils.createValue("random::5").toString();
        return (fieldPath.equals("locationNumber")) ? locationNumber : value;
    }
}
