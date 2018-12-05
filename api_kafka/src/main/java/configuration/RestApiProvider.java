package configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestApiProvider {

    private String baseUrl;
    private int port;
    private String apiName;
    private String body;
    private String contentTypeHeader;
    private String contentType;
    private String baseUrlWithPort;
}
