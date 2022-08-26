package org.yangyi.project.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.yangyi.project.domain.SysUser;

@Mapper
public interface SysUserMapper {
    int insert(SysUser row);

    @Select("select * from sys_user where username = #{username}")
    SysUser selectByUsername(@Param("username") String username);

    @Select("select * from sys_user where user_id = #{userId}")
    SysUser selectByUserId(@Param("userId") Long userId);

    int updateByUserId(SysUser row);
}