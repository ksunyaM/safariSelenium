/* Copyright 2018 Walgreen Co. */
package com.wba.test.utils.swagger;

import com.atlassian.oai.validator.OpenApiInteractionValidator;
import com.atlassian.oai.validator.model.Request;
import com.atlassian.oai.validator.model.Response;
import com.atlassian.oai.validator.model.SimpleRequest;
import com.atlassian.oai.validator.model.SimpleResponse;
import com.atlassian.oai.validator.report.ValidationReport;
import com.wba.test.utils.Utils;
import org.slf4j.Logger;

import static com.oneleo.test.automation.core.LogUtils.log;

public class SwaggerUtils {

    private static final Logger LOGGER = log(SwaggerUtils.class);

    public static boolean validateRequest(String yaml, String json, String serviceName, String methodName, StringBuilder... outputMessages) {
        return validate(yaml, json, serviceName, methodName, false, 200, outputMessages);
    }

    public static boolean validateResponse(String yaml, String json, String serviceName, String methodName, StringBuilder... outputMessages) {
        return validate(yaml, json, serviceName, methodName, true, 200, outputMessages);
    }

    public static boolean validateResponse(String yaml, String json, String serviceName, String methodName, int responceCode, StringBuilder... outputMessages) {
        return validate(yaml, json, serviceName, methodName, true, responceCode, outputMessages);
    }

    private static boolean validate(String yaml, String json, String serviceName, String methodName, boolean isResponse, int responceCode, StringBuilder... outputMessages) {
        OpenApiInteractionValidator validator = OpenApiInteractionValidator.createFor(yaml).build();
        ValidationReport validationReport;
        StringBuilder messages = Utils.defaultOrFirst(new StringBuilder(), outputMessages);
        if (isResponse) {
            validationReport = validator.validateResponse(serviceName, getMethod(methodName), convertToValidatorResponse(json, responceCode));
        } else {
            validationReport = validator.validateRequest(convertToValidatorRequest(json, serviceName, methodName));
        }
        messages.append(validationReport.getMessages().toString());
        if (validationReport.hasErrors()) {
            LOGGER.debug("FAIL: swagger validation \n" + messages.toString());
        }
        return !validationReport.hasErrors();
    }

    private static Response convertToValidatorResponse(String response, int responceCode) {
        SimpleResponse.Builder builder = SimpleResponse.Builder.status(responceCode);
        builder.withBody(response);
        builder.withContentType("application/json");
        return builder.build();
    }

    private static Request convertToValidatorRequest(String request, String serviceName, String methodName) {
        SimpleRequest.Builder builder;
        switch (methodName) {
            case "POST":
                builder = SimpleRequest.Builder.post(serviceName);
                break;
            case "PUT":
                builder = SimpleRequest.Builder.put(serviceName);
                break;
            case "GET":
            default:
                builder = SimpleRequest.Builder.get(serviceName);
        }
        builder.withBody(request);
        builder.withContentType("application/json");
        builder.withAuthorization("Basic Og==");
        return builder.build();
    }

    private static Request.Method getMethod(String methodName) {
        switch (methodName) {
            case "POST":
                return Request.Method.POST;
            case "PUT":
                return Request.Method.PUT;
            case "GET":
            default:
                return Request.Method.GET;
        }
    }
}

