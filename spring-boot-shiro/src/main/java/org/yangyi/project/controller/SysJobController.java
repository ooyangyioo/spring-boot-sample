package org.yangyi.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.yangyi.project.service.ISysJobService;

import java.util.List;

@RestController
@RequestMapping("/job")
@Validated
public class SysJobController {

    private ISysJobService sysJobService;

    @Autowired
    public ISysJobService getSysJobService() {
        return sysJobService;
    }

    /**
     * 查询定时任务列表
     */
    @GetMapping("/list")
    public List list() {
        return null;
    }

    /**
     * 查询定时任务详细信息
     */
    @GetMapping(value = "/{jobId}")
    public void info(@PathVariable("jobId") Long jobId) {
    }

    /**
     * 新增定时任务
     */
    @PostMapping
    public void add() {

    }

    /**
     * 修改定时任务
     */
    @PutMapping
    public void edit() {

    }

    /**
     * 定时任务状态修改
     */
    @PutMapping("/changeStatus")
    public void changeStatus() {
    }

    /**
     * 定时任务立即执行一次
     */
    @PutMapping("/run")
    public void run() {
    }

    /**
     * 删除定时任务
     */
    @DeleteMapping("/{jobIds}")
    public void remove(@PathVariable Long[] jobIds) {
    }

}
