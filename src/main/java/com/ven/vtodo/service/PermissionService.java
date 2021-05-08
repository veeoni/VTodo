package com.ven.vtodo.service;

import com.ven.vtodo.po.Permission;

public interface PermissionService {

    Permission savePermission(Permission tole);

    Permission getPermissionById(Long id);

    Permission getPermissionByName(String name);

    Permission updatePermission(Long id, Permission tole);

    void deletePermission(Long id);
}
