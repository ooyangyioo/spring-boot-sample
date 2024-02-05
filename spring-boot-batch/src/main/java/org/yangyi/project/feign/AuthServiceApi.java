package org.yangyi.project.feign;

import feign.Headers;
import feign.RequestLine;

import java.util.Map;

public interface AuthServiceApi {

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @RequestLine("POST /oauth")
    Map<String, Object> oauth(Map<String, Object> auth2BO);

}
