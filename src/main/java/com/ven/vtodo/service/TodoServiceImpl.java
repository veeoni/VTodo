package com.ven.vtodo.service;

import com.ven.vtodo.dao.TodoRepository;
import com.ven.vtodo.po.Todo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoServiceImpl implements TodoService {

    @Autowired
    private TodoRepository todoRepository;

    @Override
    public List<Todo> listTodo() {
        return todoRepository.findAll();
    }

    @Override
    public Todo saveTodo(Todo todo) {
        return todoRepository.save(todo);
    }

    @Override
    public Todo getTodo(Long id) {
        return todoRepository.getOne(id);
    }

    @Override
    public void deleteTodo(Long id) {
        todoRepository.deleteById(id);
    }
}
