package org.yangyi.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.yangyi.project.entity.SysMenu;
import org.yangyi.project.service.IMenuService;
import org.yangyi.project.vo.ResponseVO;

import java.util.List;

@RestController
@RequestMapping("/menu")
@Validated
public class MenuController {

    private IMenuService menuService;

    @Autowired
    public void setMenuService(IMenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping("/list")
    @ResponseBody
    @PreAuthorize("hasPermission('/menu/list', 'menu:list')")
    public ResponseEntity<ResponseVO<List<SysMenu>>> list() {
        return ResponseEntity.ok(new ResponseVO<>("1", "成功", menuService.list()));
    }

    @GetMapping("/add")
    @ResponseBody
    @PreAuthorize("hasPermission('/menu/add', 'menu:add')")
    public ResponseEntity<ResponseVO<Boolean>> add(@RequestParam String menuName,
                                                   @RequestParam String menuUrl,
                                                   @RequestParam String menuTag) {
        return ResponseEntity.ok(new ResponseVO<>("1", "成功", menuService.add(menuName, menuUrl, menuTag)));
    }

    @GetMapping("/delete")
    @ResponseBody
    @PreAuthorize("hasPermission('/menu/delete', 'menu:delete')")
    public ResponseEntity<ResponseVO<Boolean>> delete(@RequestParam String menuId) {
        return ResponseEntity.ok(new ResponseVO<>("1", "成功", menuService.delete(menuId)));
    }

}
