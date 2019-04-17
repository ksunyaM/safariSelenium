/* Copyright 2018 Walgreen Co. */
package com.wba.test.utils.datacreation;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericArray;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;

public class JsonGenerator {
    private static Logger LOGGER = LoggerFactory.getLogger(JsonGenerator.class);

    private Schema schema;
    private static final String USE_DEFAULT = "use-default";
    private boolean isOnlyMandatoryFields = false;
    private Map<String, Object> params = new HashMap<>();
    private DataGenerator data = new DataGenerator();
    private RandomDataGenerator random = new RandomDataGenerator();


    public JsonGenerator withDataTypeProvider(DataTypeProvider rules) {
        data.setRulesProvider(rules);
        return this;
    }

    public JsonGenerator withAvroSchema(String schema) {
        this.schema = new Schema.Parser().parse(schema);
        return this;
    }

    public JsonGenerator withDataTransformation(Function<String, String> dataTransformation) {
        this.data = new DataGenerator(dataTransformation);
        return this;
    }

    public JsonGenerator withOnlyMandatoryFields() {
        this.isOnlyMandatoryFields = true;
        return this;
    }

    public JsonGenerator withAllFields() {
        this.isOnlyMandatoryFields = false;
        return this;
    }

    public JsonGenerator withParameters(Map<String, Object> params) {
        this.params = params;
        return this;
    }


    public String getSchemaVersion(){
        return schema.getProp("version");
    }

    public Object generateJsonFromAvro() {
        return generate(schema, schema.getName());
    }

    @SuppressWarnings(value = "unchecked")
    private Object generate(Schema subSchema, String fieldPath) {
        switch (subSchema.getType()) {
            case RECORD:
                GenericRecord record = new GenericData.Record(subSchema);
                subSchema.getFields().forEach(field -> {
                    String path = fieldPath + "." + field.name();
                    data.setSchemaType(subSchema.getName());
                    Object value = (field.getObjectProp(USE_DEFAULT) == null)
                            ? generate(field.schema(), path)
                            : GenericData.get().getDefaultValue(field);
                    record.put(field.name(), value);
                });
                return record;
            case ENUM:
                List<String> enums = subSchema.getEnumSymbols();
                return enums.size() == 1 ? new GenericData.EnumSymbol(subSchema, enums.get(0))
                        : new GenericData.EnumSymbol(subSchema, enums.get(random.nextInt(0, enums.size() - 1)));
            case ARRAY:
                return generateArray(fieldPath, subSchema);
            case MAP:
                int length = getNumberOfElements(fieldPath.substring(0, fieldPath.lastIndexOf(".")));
                Map<Object, Object> map = new HashMap<>(length);
                for (int i = 0; i < length; i++) {
                    map.put("MAP_KEY" + i, generate(subSchema.getValueType(), fieldPath));
                }
                return map;
            case UNION:
                List<Schema> types = subSchema.getTypes();
                boolean isOptional = false;
                Schema schemaOptional = types.get(0);
                for (Schema type : types) {
                    if (type.getName().equals("null")) {
                        isOptional = true;
                    } else if (isOnlyMandatoryFields)
                        return generate(schemaOptional, fieldPath + "." + schemaOptional.getName());
                    else schemaOptional = type;
                }
                if (isOptional) {
                    Map<String, Object> json = new LinkedHashMap<>();
                    Object value = generate(schemaOptional, fieldPath + "." + schemaOptional.getName());
                    switch (schemaOptional.getType()) {
                        case ENUM: {
                            json.put(schema.getNamespace() + "." + schemaOptional.getName(), value);
                            return json;
                        }
                        case ARRAY:
                            json.put(schemaOptional.getName(), generateArray(fieldPath + "." + schemaOptional.getName(), schemaOptional));
                            return json;
                        case RECORD: {
                            json.put(schema.getNamespace() + "." + schemaOptional.getName(), generate(schemaOptional, fieldPath + "." + schemaOptional.getName()));
                            return json;
                        }
                        default:
                            json.put(schemaOptional.getName(), value);
                            return json;
                    }
                } else
                    return generate(schemaOptional, fieldPath + "." + schemaOptional.getName());
            case FIXED:
                byte[] bytes = new byte[subSchema.getFixedSize()];
                new Random().nextBytes(bytes);
                return new GenericData.Fixed(subSchema, bytes);
            case INT:
            case LONG:
            case STRING:
            case BYTES:
            case FLOAT:
            case BOOLEAN:
            case DOUBLE:
                return getData(subSchema, fieldPath);
            case NULL:
                return null;
            default:
                throw new RuntimeException("Unknown type " + subSchema);
        }
    }

    private Object getData(Schema schema, String fieldPath) {
        String typeName = schema.getLogicalType() == null ? schema.getType().getName() : schema.getLogicalType().getName();
        switch (typeName) {
            case "date": //17702 (days from epoch till current)
                return ChronoUnit.DAYS.between(LocalDate.ofEpochDay(0), LocalDate.now());
            case "timestamp-millis":// 1528394013601
                return data.generateValue(fieldPath, "timestamp::");
            case "boolean": //true
                return data.generateValue(fieldPath, "b::");
            case "string":
            case "bytes": //random digits as String with length 7
                return data.generateValue(fieldPath, "random::7");
            case "int": //random int in the range from 1 to 100
                return data.generateValue(fieldPath, "i::" + random.nextInt(1, 100));
            case "float":
            case "double": //random double from 1 to 100 in a format with two digits after dot
                return data.generateValue(fieldPath, "d::" + new DecimalFormat("##.##").format(random.nextUniform(1D, 100D)));
            case "long": //random long in a range from 10 to 100 as long
                return data.generateValue(fieldPath, "l::" + random.nextLong(10L, 100L));
            default:
                throw new RuntimeException("Unknown type " + typeName);
        }
    }

    private Integer getNumberOfElements(String path) {
        return params.keySet().contains(path) ? Integer.parseInt(params.get(path).toString()) : 1;
    }

    private GenericArray<Object> generateArray(String fieldPath, Schema subSchema) {
        int length = getNumberOfElements(fieldPath.substring(0, fieldPath.lastIndexOf(".")));
        GenericArray<Object> array = new GenericData.Array(length, subSchema);
        for (int i = 0; i < length; i++) {
            array.add(generate(subSchema.getElementType(), fieldPath + "." + subSchema.getName()));
        }
        return array;
    }

}
