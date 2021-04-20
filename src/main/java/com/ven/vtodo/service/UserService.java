package com.ven.vtodo.service;

import com.ven.vtodo.po.User;

public interface UserService {

    User checkUser(String username, String password);

    User getUserById(Long id);

    User getUserByUsername(String username);

    User saveUser(User user);

    User updateUserInfo(User user);
}
