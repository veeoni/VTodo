package com.ven.vtodo.service;

import com.ven.vtodo.po.Type;
import com.ven.vtodo.po.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TypeService {

    Type saveType(Type type);

    Type getType(Long id);

    Type getTypeByNameAndUser(String name, User user);

    Page<Type> listType(Pageable pageable, User user);

    List<Type> listTypeByUser(User user);

    List<Type> listTypeTopByUser(Integer size, User user);

    Type updateType(Long id, Type type);

    void deleteType(Long id);


}
