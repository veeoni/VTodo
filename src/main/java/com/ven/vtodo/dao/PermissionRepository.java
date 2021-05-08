package com.ven.vtodo.dao;

import com.ven.vtodo.po.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    @Query("select p from Permission p where p.name = ?1")
    Permission getPermissionByName(String name);
}
