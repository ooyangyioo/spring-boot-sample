package org.yangyi.project.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.yangyi.project.domain.SysRole;

import java.util.List;

@Mapper
public interface SysRoleMapper {

    @Delete("delete from sys_role where role_id = #{roleId}")
    int deleteById(@Param("roleId") Long roleId);

    int insert(SysRole row);

    @Select("select * from sys_role where role_id = #{roleId}")
    SysRole selectById(@Param("roleId") Long roleId);

    @Select("select * from sys_role")
    List<SysRole> list();

    @Select("select sr.* from sys_role sr left join sys_user_role sur on sur.role_id = sr.role_id where sur.user_id = #{userId}")
    List<SysRole> selectUserRoles(@Param("userId") Long userId);

    int updateById(SysRole row);

}