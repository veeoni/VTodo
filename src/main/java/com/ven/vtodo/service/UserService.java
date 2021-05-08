package com.ven.vtodo.service;

import com.ven.vtodo.po.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    User checkUser(String username, String password);

    User getUserById(Long id);

    User getUserByUsername(String username);

    User saveUser(User user);

    User updateUserInfo(User user);

    List<User> getUserByRoleId(Long id);

    List<User> listUser();

    Page<User> listUser(Pageable pageable);

    void deleteById(Long id);
}
