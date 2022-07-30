package org.yangyi.project.dao;

import org.apache.ibatis.annotations.Param;
import org.yangyi.project.entity.SysUser;
import org.yangyi.project.entity.SysUserRole;

public interface SysUserDao {

    int deleteByPrimaryKey(Long id);

    int insertSelective(SysUser record);

    /**
     * 根据用户主键查询用户基本信息
     *
     * @param id 用户主键
     * @return 用户
     */
    SysUser selectById(@Param("id") Long id);

    /**
     * 根据用户名查询用户基本信息
     *
     * @param username 用户名
     * @return 用户
     */
    SysUser selectByUsername(@Param("username") String username);

    /**
     * 查询用户及角色信息
     *
     * @param username 用户名
     * @return 用户及角色
     */
    SysUserRole selectUserAndRoleByUsername(@Param("username") String username);

    int updateByPrimaryKeySelective(SysUser record);

}