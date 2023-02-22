package org.yangyi.project.system.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.yangyi.project.system.po.SysUser;
import org.yangyi.project.system.po.SysUserRole;

@Mapper
public interface SysUserMapper {

    @Delete("delete from sys_user where user_id = #{userId}")
    int delete(Long userId);

    int insert(SysUser row);

    @Select("select * from sys_user where user_id = #{userId}")
    SysUser select(Long userId);

    int update(SysUser row);

    @Select("select * from sys_user where user_name = #{userName}")
    SysUser selectByUserName(String userName);

    SysUserRole selectUserWithRoles(@Param("userName") String userName);
}