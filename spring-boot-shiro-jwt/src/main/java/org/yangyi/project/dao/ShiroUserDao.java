package org.yangyi.project.dao;

import org.apache.ibatis.annotations.Param;
import org.yangyi.project.entity.ShiroUser;
import org.yangyi.project.entity.ShiroUserRole;

public interface ShiroUserDao {

    int deleteByPrimaryKey(Long id);

    int insertSelective(ShiroUser record);

    /**
     * 根据用户主键查询用户基本信息
     *
     * @param id 用户主键
     * @return 用户
     */
    ShiroUser selectById(@Param("id") Long id);

    /**
     * 根据用户名查询用户基本信息
     *
     * @param username 用户名
     * @return 用户
     */
    ShiroUser selectByUsername(@Param("username") String username);

    /**
     * 查询用户及角色信息
     *
     * @param username 用户名
     * @return 用户及角色
     */
    ShiroUserRole selectUserAndRoleByUsername(@Param("username") String username);

    int updateByPrimaryKeySelective(ShiroUser record);

}