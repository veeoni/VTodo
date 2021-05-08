package com.ven.vtodo.service;

import com.ven.vtodo.dao.CountdownRepository;
import com.ven.vtodo.handler.NotFoundException;
import com.ven.vtodo.po.Countdown;
import com.ven.vtodo.po.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class CountdownServiceImpl implements CountdownService {
    @Autowired
    private CountdownRepository countdownRepository;

    @Transactional
    @Override
    public Countdown saveCountdown(Countdown countdown) {
        return countdownRepository.save(countdown);
    }

    @Override
    public List<Countdown> listCountdownByUser(User user) {
        return countdownRepository.findAllByUserAndIsShowTrueOrderByCountTimeDesc(user);
    }

    @Override
    public Countdown getCountdown(Long id) {
        return countdownRepository.getOne(id);
    }

    @Override
    public Countdown getCountdownByNameAndUser(String name, User user) {
        return countdownRepository.findByNameAndUser(name, user);
    }

    @Override
    public Page<Countdown> listCountdown(Pageable pageable, User user) {
        return countdownRepository.findAllByUser(pageable, user);
    }

    @Transactional
    @Override
    public Countdown updateCountdown(Long id, Countdown countdown) {
        Countdown t = countdownRepository.getOne(id);
        if (t.getId() == null) {
            throw new NotFoundException("不存在该倒计时");
        }
        BeanUtils.copyProperties(countdown, t);
        return countdownRepository.save(countdown);
    }

    @Transactional
    @Override
    public void deleteCountdown(Long id) {
        countdownRepository.deleteById(id);
    }

    @Transactional
    @Override
    public void deleteCountdownByUser(User user) {
        countdownRepository.deleteAllByUser(user);
    }
}
