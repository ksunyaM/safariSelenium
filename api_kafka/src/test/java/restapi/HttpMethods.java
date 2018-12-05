package restapi;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class HttpMethods {

    public Response getRequest(String restApiUrl) {
        return RestAssured.get(restApiUrl);
    }

    public Response postRequest(String restApiUrl, String body) {
        return RestAssured.given().header("Content-Type", "application/json").body(body).post(restApiUrl);
    }

    public Response deleteRequest(String restApiUrl) {
        return RestAssured.delete(restApiUrl);
    }

    public Response putRequest(String restApiUrl, String body) {
        return RestAssured.given().header("Content-Type", "application/json").body(body).put(restApiUrl);
    }

}