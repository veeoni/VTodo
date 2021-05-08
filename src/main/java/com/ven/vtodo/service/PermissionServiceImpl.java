package com.ven.vtodo.service;

import com.ven.vtodo.dao.PermissionRepository;
import com.ven.vtodo.handler.NotFoundException;
import com.ven.vtodo.po.Permission;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private PermissionRepository permissionRepository;

    @Transactional
    @Override
    public Permission savePermission(Permission permission) {
        return permissionRepository.save(permission);
    }

    @Override
    public Permission getPermissionById(Long id) {
        return permissionRepository.getOne(id);
    }

    @Override
    public Permission getPermissionByName(String name) {
        return permissionRepository.getPermissionByName(name);
    }

    @Transactional
    @Override
    public Permission updatePermission(Long id, Permission permission) {
        Permission t = permissionRepository.getOne(id);
        if (t.getId() == null) {
            throw new NotFoundException("不存在该角色");
        }
        BeanUtils.copyProperties(permission, t);
        return permissionRepository.save(permission);
    }

    @Transactional
    @Override
    public void deletePermission(Long id) {
        permissionRepository.deleteById(id);
    }
}
