package org.yangyi.project.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SysIndexController {

    @RequestMapping("/")
    public String index() {
        return "YangYi";
    }

}
