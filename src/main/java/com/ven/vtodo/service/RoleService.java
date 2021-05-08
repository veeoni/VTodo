package com.ven.vtodo.service;

import com.ven.vtodo.po.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RoleService {

    Role saveRole(Role tole);

    Role getRoleById(Long id);

    Role getRoleByName(String name);

    Role updateRole(Long id, Role tole);

    void deleteRole(Long id);

    Page<Role> listRole(Pageable pageable);
}
