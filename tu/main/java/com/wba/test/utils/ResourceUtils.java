/*
 * Copyright 2018 Walgreen Co.
 */
package com.wba.test.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import cucumber.runtime.CucumberException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Optional;
import java.util.stream.Collectors;

public class ResourceUtils {

    private static final ObjectMapper YML_MAPPER = new ObjectMapper(new YAMLFactory());

    public static String getResourceAsString(String path) {

        final URL url = Optional.ofNullable(ResourceUtils.class.getClassLoader().getResource(path))
                .orElseThrow(() -> new CucumberException("There is no resource by path: " + path));

        try {
            return new String(Files.readAllBytes(Paths.get(url.toURI())));
        } catch (IOException | URISyntaxException e) {
            throw new CucumberException("Resource reading error (" + path + "):" + e);
        }
    }

    public static <T> T readYamlResource(String path, Class<T> clazz) throws IOException {
        return YML_MAPPER.readValue(getResourceAsString(path), clazz);
    }

    public static String getResourcePath(String rootDirectory, String resourceName) {
        try {
            LinkedList<String> result = FileUtils.listFiles(new File(ResourceUtils.class.getClassLoader().getResource(rootDirectory).getPath()), null, true).stream()
                    .filter(file -> file.getName().equalsIgnoreCase(resourceName))
                    .map(File::toURI)
                    .map(URI::getPath)
                    .collect(Collectors.toCollection(LinkedList::new));

            if (result.isEmpty()) {
                throw new RuntimeException("File " + StringUtils.defaultIfEmpty(resourceName, "null/empty") +" is not found in the root directory " + rootDirectory);
            } else if (result.size() > 1) {
                throw new RuntimeException("More then one file with the name " + resourceName + " was found in the root directory " + rootDirectory);
            } else {
                return result.getFirst().substring(result.getFirst().indexOf(rootDirectory));
            }

        } catch (NullPointerException e) {
            throw new RuntimeException("Root directory " + rootDirectory + " is not found");
        }
    }
}
