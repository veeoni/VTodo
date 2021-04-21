package com.ven.vtodo.dao;

import com.ven.vtodo.po.Tag;
import com.ven.vtodo.po.Todo;
import com.ven.vtodo.po.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag,Long>  {
    Tag findByNameAndUser(String name, User user);
    @Query("select t from Tag t")
    List<Tag> findTop(Pageable pageable);

    @Query("select t from Tag t LEFT JOIN t.user u where u = ?1")
    List<Tag> findTopByUser(User user, Pageable pageable);

    Page<Tag> findAllByUser(Pageable pageable, User user);

    List<Tag> findAllByUser(User user);

    List<Tag> findAllByTodos(Todo todo);
}
