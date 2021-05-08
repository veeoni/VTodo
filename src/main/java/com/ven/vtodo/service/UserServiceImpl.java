package com.ven.vtodo.service;

import com.ven.vtodo.dao.UserRepository;
import com.ven.vtodo.po.User;
import com.ven.vtodo.util.SHA256Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public User checkUser(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, SHA256Util.getSHA256(username + password));
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.getOne(id);
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    @Override
    public User saveUser(User user) {
        Date date = new Date();
        if (user.getId() == null) {
            user.setCreateTime(date);
            user.setUpdateTime(date);
            user.setType(0);//默认为非管理员
        } else {
            user.setUpdateTime(date);
        }
        return userRepository.save(user);
    }

    @Override
    public User updateUserInfo(User user) {
        return userRepository.save(user);
    }

    @Override
    public List<User> getUserByRoleId(Long id) {
        return userRepository.findAllByRoleId(id);
    }

    @Override
    public List<User> listUser() {
        return userRepository.findAll();
    }

    @Override
    public Page<User> listUser(Pageable pageable) {
        return userRepository.findAll(pageable);
    }
}
