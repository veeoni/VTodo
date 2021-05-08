package com.ven.vtodo.service;

import com.ven.vtodo.dao.TypeRepository;
import com.ven.vtodo.handler.NotFoundException;
import com.ven.vtodo.po.Type;
import com.ven.vtodo.po.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class TypeServiceImpl implements TypeService {
    @Autowired
    private TypeRepository typeRepository;

    @Transactional
    @Override
    public Type saveType(Type type) {
        return typeRepository.save(type);
    }

    @Override
    public Type getType(Long id) {
        return typeRepository.getOne(id);
    }

    @Override
    public Type getTypeByNameAndUser(String name, User user) {
        return typeRepository.findByNameAndUser(name, user);
    }

    @Override
    public Page<Type> listType(Pageable pageable, User user) {
        return typeRepository.findAllByUser(pageable, user);
    }

    @Override
    public List<Type> listTypeTopByUser(Integer size, User user) {
        Sort sort = Sort.by(Sort.Direction.DESC, "notes.size");
        Pageable pageable = PageRequest.of(0, size, sort);
        return typeRepository.findTopByUser(user, pageable);
    }

    @Override
    public List<Type> listTypeByUser(User user) {
        return typeRepository.findAllByUser(user);
    }

    @Transactional
    @Override
    public Type updateType(Long id, Type type) {
        Type t = typeRepository.getOne(id);
        if (t.getId() == null) {
            throw new NotFoundException("不存在该分类");
        }
        BeanUtils.copyProperties(type, t);
        return typeRepository.save(type);
    }

    @Transactional
    @Override
    public void deleteType(Long id) {
        typeRepository.deleteById(id);
    }

    @Transactional
    @Override
    public void deleteTypeByUser(User user) {
        typeRepository.deleteAllByUser(user);
    }
}
