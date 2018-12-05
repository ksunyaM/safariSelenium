package restapi.drugs;

import configuration.ApiMicroservicesType;
import configuration.ApiUtils;
import configuration.TestDataUtils;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import restapi.HttpMethods;


public class DrugsBase {

    private HttpMethods httpMethods = new HttpMethods();
    private TestDataUtils testDataUtils = new TestDataUtils();
    private ApiUtils apiUtils = new ApiUtils();
    private String realDrugsUrl = apiUtils.getApiUrl(ApiMicroservicesType.DRUGS);

    public Response postRequestCreateDrug(String pathFile) {
        JSONObject jsonObject = testDataUtils.mappingJsonToObject(pathFile);
        return httpMethods.postRequest(realDrugsUrl, jsonObject.toJSONString());
    }

    public String resposeDrugIdAfterPostCreateDrug(Response response) {
        return response.body().jsonPath().get("id").toString();
    }

}
