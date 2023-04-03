package org.yangyi.project.system.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.yangyi.project.system.po.SysRole;
import org.yangyi.project.system.po.SysRoleMenu;

import java.util.List;

@Mapper
public interface SysRoleMapper {

    @Delete("delete from sys_role where role_id = #{roleId")
    int delete(Long roleId);

    int insert(SysRole row);

    @Select("select * from sys_role where role_id = #{roleId}")
    SysRole select(Long roleId);

    int update(SysRole row);

    List<SysRole> selectUserRoles(@Param("userId") Long userId);

    SysRoleMenu selectRoleWithMenu(@Param("roleId") Long roleId);
}