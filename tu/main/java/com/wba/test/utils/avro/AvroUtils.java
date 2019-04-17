/* Copyright 2018 Walgreen Co. */
package com.wba.test.utils.avro;

import com.wba.test.utils.Utils;
import org.apache.avro.AvroTypeException;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.slf4j.Logger;

import java.io.*;

import static com.oneleo.test.automation.core.LogUtils.log;

public class AvroUtils {

    private static final Logger LOGGER = log(AvroUtils.class);

    public static boolean validate(String avro, String json, StringBuilder... outputMessages) {
        Schema schema = new Schema.Parser().parse(avro);
        InputStream input = new ByteArrayInputStream(json.getBytes());
        DataInputStream din = new DataInputStream(input);

        StringBuilder messages = Utils.defaultOrFirst(new StringBuilder(), outputMessages);
        boolean validationResult;

        try {
            DatumReader reader = new GenericDatumReader(schema);
            Decoder decoder = DecoderFactory.get().jsonDecoder(schema, din);
            reader.read(null, decoder);
            validationResult = true;
        } catch (Exception e) {
            messages.append(e.getMessage());
            validationResult = false;
        }

        if (!validationResult) {
            LOGGER.debug("FAIL: avro validation \n" + messages.toString());
        }

        return validationResult;
    }
}
