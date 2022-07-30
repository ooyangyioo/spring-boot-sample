package org.yangyi.project.service;

import cn.hutool.core.lang.Snowflake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yangyi.project.dao.ShiroRoleDao;
import org.yangyi.project.entity.ShiroRole;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RoleService {

    private final ShiroRoleDao shiroRoleDao;
    private final Snowflake snowflake;

    @Autowired(required = false)
    public RoleService(ShiroRoleDao shiroRoleDao) {
        this.shiroRoleDao = shiroRoleDao;
        snowflake = new Snowflake(1, 1);
    }

    public List<ShiroRole> roleList() {
        return shiroRoleDao.selectShiroRoles();
    }

    public boolean roleAdd(String roleCode) {
        ShiroRole shiroRole = new ShiroRole();
        shiroRole.setId(snowflake.nextId());
        shiroRole.setRoleCode(roleCode);
        shiroRole.setRoleDesc(roleCode);
        shiroRole.setRoleName(roleCode);
        boolean result = shiroRoleDao.insertShiroRole(shiroRole) == 0 ? false : true;
        return result;
    }

    public boolean roleDelete() {
        return false;
    }

    public boolean roleUpdate() {
        return false;
    }

    public Set<String> getRolesByUserId(Long uid) {
        Set<String> roles = new HashSet<>();
        //三种编程语言代表三种角色：js程序员、java程序员、c++程序员
        roles.add("js");
        roles.add("java");
        roles.add("cpp");
        return roles;
    }

}
