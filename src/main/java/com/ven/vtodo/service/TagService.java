package com.ven.vtodo.service;

import com.ven.vtodo.po.Tag;
import com.ven.vtodo.po.Todo;
import com.ven.vtodo.po.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TagService {

    Tag saveTag(Tag tag);

    Tag getTag(Long id);

    Tag getTagByNameAndUser(String name, User user);

    Page<Tag> listTag(Pageable pageable, User user);

    List<Tag> listTagByUser(User user);

    List<Tag> listTagByTodo(Todo todo);

    List<Tag> listTag(String id);

    List<Tag> listTagTopByUser(Integer size, User user);

    Tag updateTag(Long id, Tag tag);

    void deleteTag(Long id);

    void deleteTagByUser(User user);
}
