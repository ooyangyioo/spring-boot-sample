package org.yangyi.project.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.yangyi.project.domain.SysUser;

@Mapper
public interface SysUserMapper {

//    int deleteByPrimaryKey(Long userId);
//
//    int insertSelective(SysUser row);
//
//    SysUser selectByPrimaryKey(Long userId);
//
//    int updateByPrimaryKeySelective(SysUser row);

    @Select("select * from sys_user where user_name = #{userName}")
    SysUser selectByUsername(@Param("userName") String userName);

    int insert(SysUser row);

    @Update("update sys_user set account_locked = #{userName} where user_name = #{userName}")
    int userLockStatus(@Param("userName") String userName, @Param("lockStatus") Integer lockStatus);

}