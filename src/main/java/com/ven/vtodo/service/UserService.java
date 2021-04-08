package com.ven.vtodo.service;

import com.ven.vtodo.po.User;

public interface UserService {

    User checkUser(String username, String password);
}
