package com.ven.vtodo.util;

import com.ven.vtodo.po.Tag;
import com.ven.vtodo.po.Todo;
import com.ven.vtodo.service.TagService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class TodoCopy {
    @Autowired
    private static TagService tagService;

    public static Todo copyTodo(Todo todo){
        Todo todoCpy = new Todo();
        BeanUtils.copyProperties(todo, todoCpy);
        List<Tag> tags = tagService.listTagByTodo(todo);
        List<Tag> newTags = new ArrayList<>(tags.size());
        tags.forEach(tag -> newTags.add(tag));
        todoCpy.setTags(newTags);
        return todoCpy;
    }
}
