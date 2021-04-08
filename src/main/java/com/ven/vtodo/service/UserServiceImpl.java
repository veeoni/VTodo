package com.ven.vtodo.service;

import com.ven.vtodo.dao.UserRepository;
import com.ven.vtodo.po.User;
import com.ven.vtodo.util.SHA256Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public User checkUser(String username, String password) {
        User user = userRepository.findByUsernameAndPassword(username, SHA256Util.getSHA256(username+password));
        return user;
    }
}
