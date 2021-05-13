package com.ven.vtodo.interceptor;


import com.ven.vtodo.po.Permission;
import com.ven.vtodo.po.Role;
import com.ven.vtodo.po.User;
import com.ven.vtodo.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static com.ven.vtodo.util.PermissionUtils.hasPermission;

@Component
public class TypeInterceptor implements HandlerInterceptor {

    @Autowired
    private PermissionService permissionService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        User user = (User) request.getSession().getAttribute("user");
        if ( user == null || user.getRole() == null) {
            response.sendRedirect("/login");
            return false;
        }
        Permission permission = permissionService.getPermissionByName("分类管理");
        Role role = user.getRole();
        List<Permission> permissions = permissionService.getPermissionsOfRole(role);
        if(permission!=null && hasPermission(permissions, permission)){
            return true;
        }
        response.sendRedirect("/cannotaccess");
        return false;
    }
}
