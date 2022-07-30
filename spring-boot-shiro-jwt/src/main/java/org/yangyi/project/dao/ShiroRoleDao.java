package org.yangyi.project.dao;

import org.apache.ibatis.annotations.Param;
import org.yangyi.project.entity.ShiroRole;

import java.util.List;

public interface ShiroRoleDao {
    int deleteShiroRole(Long id);

    int insertShiroRole(ShiroRole record);

    /**
     * 查询全部的权限列表
     *
     * @return
     */
    List<ShiroRole> selectShiroRoles();

    /**
     * 查询用户的角色信息
     *
     * @param userId 用户主键
     * @return 用户所拥有的角色列表
     */
    List<ShiroRole> selectUserShiroRoles(@Param("userId") Long userId);

    int updateShiroRole(ShiroRole record);
}