package org.yangyi.project.dao;

import org.yangyi.project.entity.ShiroPermission;

import java.util.List;

public interface ShiroPermissionDao {

    int deletePermission(Long id);

    int insertPermission(ShiroPermission record);

    ShiroPermission selectPermission(Long id);

    List<ShiroPermission> selectPermissions();

    int updatePermission(ShiroPermission record);

}