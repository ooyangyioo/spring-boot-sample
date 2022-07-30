package org.yangyi.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yangyi.project.dao.ShiroPermissionDao;
import org.yangyi.project.entity.ShiroPermission;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PermissionService {

    private final ShiroPermissionDao shiroPermissionDao;

    @Autowired(required = false)
    public PermissionService(ShiroPermissionDao shiroPermissionDao) {
        this.shiroPermissionDao = shiroPermissionDao;
    }

    public List<ShiroPermission> permissionList() {
        List<ShiroPermission> shiroPermissions = shiroPermissionDao.selectPermissions();
        return shiroPermissions;
    }

    public boolean permissionAdd() {
        return false;
    }

    public Set<String> getPermsByUserId(Long uid) {
        Set<String> perms = new HashSet<>();
        //三种编程语言代表三种角色：js程序员、java程序员、c++程序员
        //js程序员的权限
        perms.add("html:edit");
        //c++程序员的权限
        perms.add("hardware:debug");
        //java程序员的权限
        perms.add("mvn:install");
        perms.add("mvn:clean");
        perms.add("mvn:test");
        return perms;
    }

}
