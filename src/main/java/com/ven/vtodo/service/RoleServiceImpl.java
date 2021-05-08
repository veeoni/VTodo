package com.ven.vtodo.service;

import com.ven.vtodo.dao.RoleRepository;
import com.ven.vtodo.handler.NotFoundException;
import com.ven.vtodo.po.Role;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Transactional
    @Override
    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public Role getRoleById(Long id) {
        return roleRepository.getOne(id);
    }

    @Override
    public Role getRoleByName(String name) {
        return roleRepository.getRoleByName(name);
    }

    @Transactional
    @Override
    public Role updateRole(Long id, Role role) {
        Role t = roleRepository.getOne(id);
        if (t.getId() == null) {
            throw new NotFoundException("不存在该角色");
        }
        BeanUtils.copyProperties(role, t);
        return roleRepository.save(role);
    }

    @Override
    public Page<Role> listRole(Pageable pageable) {
        return roleRepository.findAll(pageable);
    }


    @Transactional
    @Override
    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }
}
