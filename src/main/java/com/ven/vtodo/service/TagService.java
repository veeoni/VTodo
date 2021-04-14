package com.ven.vtodo.service;

import com.ven.vtodo.po.Tag;
import com.ven.vtodo.po.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TagService {

    Tag saveTag(Tag tag);

    Tag getTag(Long id);

    Tag getTagByName(String name);

    Page<Tag> listTag(Pageable pageable);

    List<Tag> listTag();

    List<Tag> listTagByTodo(Todo todo);

    List<Tag> listTag(String id);

    List<Tag> listTagTop(Integer size);

    Tag updateTag(Long id, Tag tag);

    void deleteTag(Long id);
}
