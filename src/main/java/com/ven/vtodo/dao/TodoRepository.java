package com.ven.vtodo.dao;

import com.ven.vtodo.po.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Date;
import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long>, JpaSpecificationExecutor<Todo> {

    List<Todo> findAllByTaskDateLessThanEqualAndFinishedDateNull(Date date);

    List<Todo> findAllByTaskDateEqualsAndFinishedDateNull(Date date);

    List<Todo> findAllByFinishedDateEquals(Date date);
}
