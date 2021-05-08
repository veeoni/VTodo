package com.ven.vtodo.service;

import com.ven.vtodo.po.Countdown;
import com.ven.vtodo.po.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CountdownService {

    Countdown saveCountdown(Countdown countdown);

    Countdown getCountdown(Long id);

    Countdown getCountdownByNameAndUser(String name, User user);

    Page<Countdown> listCountdown(Pageable pageable, User user);

    List<Countdown> listCountdownByUser(User user);

    Countdown updateCountdown(Long id, Countdown countdown);

    void deleteCountdown(Long id);

    void deleteCountdownByUser(User user);
}
