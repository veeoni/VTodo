package com.ven.vtodo.dao;

import com.ven.vtodo.po.Todo;
import com.ven.vtodo.po.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Date;
import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long>, JpaSpecificationExecutor<Todo> {

    List<Todo> findAllByUserAndTaskDateLessThanEqualAndFinishedDateNull(User user, Date date);

    List<Todo> findAllByUserAndTaskDateEqualsAndFinishedDateNull(User user, Date date);

    List<Todo> findAllByUserAndFinishedDateEquals(User user, Date date);

    void deleteAllByUser(User user);
}
