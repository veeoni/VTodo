package com.ven.vtodo.dao;

import com.ven.vtodo.po.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsernameAndPassword(String username, String password);

    User findUserByUsername(String username);

    @Query("select u from User u where u.role.id = ?1")
    List<User> findAllByRoleId(Long id);
}
