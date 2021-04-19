package com.ven.vtodo.dao;

import com.ven.vtodo.po.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsernameAndPassword(String username, String password);

    User findUserByUsername(String username);
}
