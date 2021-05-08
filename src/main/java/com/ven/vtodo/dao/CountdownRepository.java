package com.ven.vtodo.dao;

import com.ven.vtodo.po.Countdown;
import com.ven.vtodo.po.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CountdownRepository extends JpaRepository<Countdown, Long> {
    Countdown findByNameAndUser(String name, User user);

    @Query("select c from Countdown c LEFT JOIN c.user u where u = ?1")
    List<Countdown> findTopByUser(User user, Pageable pageable);

    Page<Countdown> findAllByUser(Pageable pageable, User user);

    List<Countdown> findAllByUserAndIsShowTrueOrderByCountTimeDesc(User user);

    void deleteAllByUser(User user);
}
