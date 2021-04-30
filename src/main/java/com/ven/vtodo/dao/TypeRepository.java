package com.ven.vtodo.dao;

import com.ven.vtodo.po.Type;
import com.ven.vtodo.po.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TypeRepository extends JpaRepository<Type, Long> {
    Type findByNameAndUser(String name, User user);

    //JpaRepository未提供的方法，可以自己按照restful风格创建。
    @Query("select t from Type t LEFT JOIN t.user u where u = ?1")
    List<Type> findTopByUser(User user, Pageable pageable);

    Page<Type> findAllByUser(Pageable pageable, User user);

    List<Type> findAllByUser(User user);
}
