package com.ven.vtodo.util;

import com.ven.vtodo.po.Permission;

import java.util.List;
import java.util.Objects;

public class PermissionUtils {

    public static boolean hasPermission(List<Permission> permissions, Permission permission){
        for (Permission p : permissions) {
            if(Objects.equals(p.getId(), permission.getId())){
                return true;
            }
        }
        return false;
    }
}
