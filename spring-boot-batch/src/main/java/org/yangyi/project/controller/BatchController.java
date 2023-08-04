package org.yangyi.project.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yangyi.project.vo.BatchVO;

@RestController
public class BatchController {

    @GetMapping(value = "/batch", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public BatchVO batchController() {
        return new BatchVO() {{
            setId(11111111);
            setName("杨毅");
        }};
    }

}
