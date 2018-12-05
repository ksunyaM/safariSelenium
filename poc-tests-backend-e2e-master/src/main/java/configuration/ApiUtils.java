package configuration;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;

@Slf4j
public class ApiUtils {

    private final String PATIENTS_ENDPOINT_URL = "patients.baseApiUrl";
    private final String PRESCRIPTIONS_ENDPOINT_URL = "prescriptions.baseApiUrl";
    private final String DRUGS_ENDPOINT_URL = "drugs.baseApiUrl";
    private final String SEARCH_ENDPOINT_URL = "search.baseApiUrl";
    private final String DOCTORS_ENDPOINT_URL = "doctors.baseApiUrl";
    private final String VISITS_ENDPOINT_URL = "visits.baseApiUrl";

    private Properties properties = new Properties();

    public ApiUtils() {

        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String appConfigPath = rootPath + "default-api.properties";

        try {
            File file = new File(appConfigPath);
            FileInputStream fileInput = new FileInputStream(file);
            properties.load(fileInput);
            fileInput.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getApiUrl(ApiMicroservicesType type) {

        String apiUrl = "http://localhost:8080";

        switch(type){
            case PATIENTS:
                apiUrl = properties.getProperty(PATIENTS_ENDPOINT_URL);
                break;
            case PRESCRIPTIONS:
                apiUrl = properties.getProperty(PRESCRIPTIONS_ENDPOINT_URL);
                break;
            case DRUGS:
                apiUrl = properties.getProperty(DRUGS_ENDPOINT_URL);
                break;
            case SEARCH:
                apiUrl = properties.getProperty(SEARCH_ENDPOINT_URL);
                break;
            case DOCTORS:
                apiUrl = properties.getProperty(DOCTORS_ENDPOINT_URL);
                break;
            case VISITS:
                apiUrl = properties.getProperty(VISITS_ENDPOINT_URL);
                break;
        }

        log.info("Endpoint is set for " + type + " on url " + apiUrl);
        return apiUrl;
    }

}
