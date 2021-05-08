package com.ven.vtodo.service;

import com.ven.vtodo.po.Target;
import com.ven.vtodo.po.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TargetService {

    Target saveTarget(Target target);

    Target getTarget(Long id);

    Target getTargetByNameAndUser(String name, User user);

    Page<Target> listTarget(Pageable pageable, User user);

    List<Target> listTargetByUser(User user);

    Target updateTarget(Long id, Target target);

    void deleteTarget(Long id);

    void deleteTargetByUser(User user);
}
