package org.yangyi.project.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yangyi.project.system.dto.MenuAddDTO;
import org.yangyi.project.system.service.ISysMenuService;
import org.yangyi.project.web.ResponseVO;

@RestController
@RequestMapping("/menu")
public class SysMenuController {

    private final ISysMenuService sysMenuService;

    @Autowired
    public SysMenuController(ISysMenuService sysMenuService) {
        this.sysMenuService = sysMenuService;
    }

    @PostMapping("/query")
    public ResponseVO query() {
        return null;
    }

    @PostMapping("/list")
    public ResponseVO list() {
        return null;
    }

    @PostMapping("/add")
    public ResponseVO add(@RequestBody @Validated MenuAddDTO menuAddDTO) {

        return null;
    }

    @PostMapping("/edit")
    public ResponseVO edit() {
        return null;
    }

    @PostMapping("/remove")
    public ResponseVO remove() {
        return null;
    }

}
