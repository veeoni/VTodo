package com.ven.vtodo.service;

import com.ven.vtodo.po.Permission;
import com.ven.vtodo.po.Role;

import java.util.List;

public interface PermissionService {

    Permission savePermission(Permission tole);

    Permission getPermissionById(Long id);

    Permission getPermissionByName(String name);

    Permission updatePermission(Long id, Permission tole);

    void deletePermission(Long id);

    List<Permission> getAll();

    List<Permission> listPermissionByIds(String permissionIds);

    List<Permission> getPermissionsOfRole(Role role);
}
