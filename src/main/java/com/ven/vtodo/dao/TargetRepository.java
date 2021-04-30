package com.ven.vtodo.dao;

import com.ven.vtodo.po.Target;
import com.ven.vtodo.po.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TargetRepository extends JpaRepository<Target, Long> {
    Target findByNameAndUser(String name, User user);

    @Query("select t from Target t LEFT JOIN t.user u where u = ?1")
    List<Target> findTopByUser(User user, Pageable pageable);

    Page<Target> findAllByUser(Pageable pageable, User user);

    List<Target> findAllByUserAndIsShowTrue(User user);
}
