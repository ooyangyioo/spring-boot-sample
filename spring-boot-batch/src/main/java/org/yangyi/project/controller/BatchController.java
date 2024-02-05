package org.yangyi.project.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yangyi.project.feign.AuthServiceApi;
import org.yangyi.project.vo.BatchVO;

import java.util.HashMap;
import java.util.Map;

@RestController
public class BatchController {

    private static final Logger log = LoggerFactory.getLogger(BatchController.class);

    private AuthServiceApi authServiceApi;

    @GetMapping(value = "/batch", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public BatchVO batchController() {

        log.info("调用远程服务");
        Map<String, Object> responseData = authServiceApi.oauth(new HashMap<>() {{
            put("phone", "18000011005");
            put("name", "杨毅");
            put("age", 20);
            put("userType", 1);
            put("amount", 1);
        }});
        log.info("远程服务响应：{}", responseData);
        return new BatchVO() {{
            setId(11111111);
            setName("杨毅");
        }};
    }

    @Autowired
    @Qualifier("asyncAuthServiceApi")
    public void setAuthServiceApi(AuthServiceApi authServiceApi) {
        this.authServiceApi = authServiceApi;
    }
}
