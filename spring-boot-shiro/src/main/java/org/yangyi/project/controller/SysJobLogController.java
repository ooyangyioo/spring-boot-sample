package org.yangyi.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yangyi.project.service.ISysJobLogService;

@RestController
@RequestMapping("/job_log")
@Validated
public class SysJobLogController {

    private ISysJobLogService sysJobLogService;

    @Autowired
    public void setSysJobLogService(ISysJobLogService sysJobLogService) {
        this.sysJobLogService = sysJobLogService;
    }

}
