package configuration;

import com.github.tomakehurst.wiremock.WireMockServer;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static wiremock.org.apache.http.HttpStatus.SC_OK;

public class StubProvider {

    @Deprecated
    public void setupStubBaseUrl(RestApiProvider provider) {
        provider.setBaseUrl("http://localhost:8080");
    }

    /**
     * localhost shouldn't be set in this way!!!!!!!!!!!!!!!!!
     */
    public String setupBaseUrlWithPort(RestApiProvider provider) {
        provider.setBaseUrlWithPort(provider.getBaseUrlWithPort());
        provider.setBaseUrlWithPort("http://localhost:8080");

        return String.format("%s", provider.getBaseUrlWithPort());
    }

    public void filterByHttpMethodsForMockedData(HttpMethodsType httpMethod, WireMockServer wireMockRule, RestApiProvider provider, int responseStatus) {

        switch (httpMethod) {
            case GET:
                wireMockRule.stubFor(get(urlEqualTo(provider.getApiName()))
                        .willReturn(aResponse()
                                .withHeader(provider.getContentTypeHeader(), provider.getContentType())
//                                .withBody(provider.getBody())
                                .withStatus(responseStatus)));
                break;
            case POST:
                wireMockRule.stubFor(post(urlEqualTo(provider.getBaseUrlWithPort() + provider.getApiName()))
                        .willReturn(aResponse()
                                .withHeader(provider.getContentTypeHeader(), provider.getContentType())
                                .withBody(provider.getBody())
                                .withStatus(responseStatus)));

//                service.stubFor(post(urlEqualTo(provider.getBaseUrlWithPort() + "/visits"))
////                .willReturn(aResponse()
////                        .withHeader(provider.getContentTypeHeader(), provider.getContentType())
////                        .withBody(provider.getBody())
////                        .withStatus(200)));
//


                break;
            case PUT:
                break;
            case DELETE:
                wireMockRule.stubFor(delete(urlEqualTo(provider.getApiName()))
                        .willReturn(aResponse()
                                .withHeader(provider.getContentTypeHeader(), provider.getContentType())
                                .withStatus(responseStatus)));
                break;
        }

    }


    public String stubEndpointForGet(String bodyBuildedFromObject, RestApiProvider provider, WireMockServer wireMockRule) {
        //provider.setBaseUrlWithPort(endpoint);
        provider.setContentTypeHeader("application/json");
        provider.setContentType("Content-Type");
        provider.setBody(bodyBuildedFromObject);
        // String stubUrl = provider.getBaseUrlWithPort();
        this.filterByHttpMethodsForMockedData(HttpMethodsType.GET, wireMockRule, provider, SC_OK);
        return provider.getBaseUrlWithPort();
    }

    public String stubEndpointForPost(String bodyBuildedFromObject, RestApiProvider provider, WireMockServer wireMockRule) {
        //provider.setBaseUrlWithPort(endpoint);
        provider.setContentTypeHeader("application/json");
        provider.setContentType("Content-Type");
        provider.setBody(bodyBuildedFromObject);
        // String stubUrl = provider.getBaseUrlWithPort();
        this.filterByHttpMethodsForMockedData(HttpMethodsType.POST, wireMockRule, provider, SC_OK);
        return provider.getBaseUrlWithPort();
    }


}

