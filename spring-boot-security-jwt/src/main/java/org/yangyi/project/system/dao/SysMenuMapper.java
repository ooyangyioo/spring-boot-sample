package org.yangyi.project.system.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.yangyi.project.system.po.SysMenu;

@Mapper
public interface SysMenuMapper {

    @Delete("delete from sys_menu where menu_id = #{menuId}")
    int delete(Long menuId);

    int insert(SysMenu row);

    @Select("select * from sys_menu where menu_id = #{menuId}")
    SysMenu select(Long menuId);

    int update(SysMenu row);

}