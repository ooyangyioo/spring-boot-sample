package org.yangyi.project.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.yangyi.project.domain.SysMenu;
import org.yangyi.project.domain.SysMenuTree;

import java.util.List;

@Mapper
public interface SysMenuMapper {

    @Delete("delete from sys_menu where menu_id = #{menuId}")
    int deleteById(@Param("menuId") Long menuId);

    int insert(SysMenu row);

    int update(SysMenu row);

    List<SysMenuTree> selectTreeByParentId(@Param("menuId") Long menuId);

    @Select("select sm.* from sys_menu sm left join sys_role_menu srm on srm.menu_id = sm.menu_id where srm.role_id = #{roleId}")
    List<SysMenu> selectByRoleId(@Param("roleId") Long roleId);
}