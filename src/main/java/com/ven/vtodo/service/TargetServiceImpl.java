package com.ven.vtodo.service;

import com.ven.vtodo.dao.TargetRepository;
import com.ven.vtodo.handler.NotFoundException;
import com.ven.vtodo.po.Target;
import com.ven.vtodo.po.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class TargetServiceImpl implements TargetService {
    @Autowired
    private TargetRepository targetRepository;

    @Transactional
    @Override
    public Target saveTarget(Target target) {
        return targetRepository.save(target);
    }

    @Override
    public List<Target> listTargetByUser(User user) {
        return targetRepository.findAllByUserAndIsShowTrue(user);
    }

    @Override
    public Target getTarget(Long id) {
        return targetRepository.getOne(id);
    }

    @Override
    public Target getTargetByNameAndUser(String name, User user) {
        return targetRepository.findByNameAndUser(name, user);
    }

    @Override
    public Page<Target> listTarget(Pageable pageable, User user) {
        return targetRepository.findAllByUser(pageable, user);
    }

    @Transactional
    @Override
    public Target updateTarget(Long id, Target target) {
        Target t = targetRepository.getOne(id);
        if (t.getId() == null) {
            throw new NotFoundException("不存在该目标");
        }
        BeanUtils.copyProperties(target, t);
        return targetRepository.save(target);
    }

    @Transactional
    @Override
    public void deleteTarget(Long id) {
        targetRepository.deleteById(id);
    }

    @Transactional
    @Override
    public void deleteTargetByUser(User user) {
        targetRepository.deleteAllByUser(user);
    }
}
