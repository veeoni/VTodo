package com.ven.vtodo.service;

import com.ven.vtodo.dao.TagRepository;
import com.ven.vtodo.handler.NotFoundException;
import com.ven.vtodo.po.Tag;
import com.ven.vtodo.po.Todo;
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

import static com.ven.vtodo.util.ListToStringUtil.idsToList;


@Service
public class TagServiceImpl implements TagService {
    @Autowired
    private TagRepository tagRepository;

    @Override
    public List<Tag> listTagTopByUser(Integer size, User user) {
        Sort sort = Sort.by(Sort.Direction.DESC, "notes.size");
        Pageable pageable = PageRequest.of(0, size, sort);
        return tagRepository.findTopByUser(user, pageable);
    }

    @Transactional
    @Override
    public Tag saveTag(Tag tag) {
        return tagRepository.save(tag);
    }

    @Override
    public List<Tag> listTagByUser(User user) {
        return tagRepository.findAllByUser(user);
    }

    @Override
    public List<Tag> listTag(String id) {
        return tagRepository.findAllById(idsToList(id));
    }

    @Override
    public Tag getTag(Long id) {
        return tagRepository.getOne(id);
    }

    @Override
    public Tag getTagByNameAndUser(String name, User user) {
        return tagRepository.findByNameAndUser(name, user);
    }

    @Override
    public Page<Tag> listTag(Pageable pageable, User user) {
        return tagRepository.findAllByUser(pageable, user);
    }

    @Override
    public List<Tag> listTagByTodo(Todo todo) {
        return tagRepository.findAllByTodos(todo);
    }

    @Transactional
    @Override
    public Tag updateTag(Long id, Tag tag) {
        Tag t = tagRepository.getOne(id);
        if (t.getId() == null) {
            throw new NotFoundException("不存在该标签");
        }
        BeanUtils.copyProperties(tag, t);
        return tagRepository.save(tag);
    }

    @Transactional
    @Override
    public void deleteTag(Long id) {
        tagRepository.deleteById(id);
    }
}
