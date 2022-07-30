package org.yangyi.project.dao;

import org.apache.ibatis.annotations.Param;
import org.yangyi.project.entity.SysRole;

import java.util.List;

public interface SysRoleDao {
    int deleteSysRole(Long id);

    int insertSysRole(SysRole record);

    /**
     * 查询全部的权限列表
     *
     * @return
     */
    List<SysRole> selectSysRoles();

    /**
     * 查询用户的角色信息
     *
     * @param userId 用户主键
     * @return 用户所拥有的角色列表
     */
    List<SysRole> selectUserRole(@Param("userId") Long userId);

    int updateSysRole(SysRole record);
}