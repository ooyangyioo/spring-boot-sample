package org.yangyi.project.system.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.yangyi.project.system.po.SysUser;

@Mapper
public interface SysUserMapper {
    int deleteByUserId(Long userId);

    int insert(SysUser row);

    @Select("select * from sys_user where user_id = #{userId}")
    SysUser selectByUserId(Long userId);

    @Select("select * from sys_user where user_name = #{userName}")
    SysUser selectByUserName(String userName);

    int updateByUserId(SysUser row);

}