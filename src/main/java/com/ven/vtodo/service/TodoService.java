package com.ven.vtodo.service;

import com.ven.vtodo.po.Todo;

import java.util.Date;
import java.util.List;

public interface TodoService {

    Todo saveTodo(Todo todo);

    Todo saveFinishedTodo(Todo todo);

    Todo getTodo(Long id);

    List<Todo> listTodo();

    List<Todo> listUnfinishedTodosByDate(Date date);

    List<Todo> listFinishedTodosByDate(Date date);

    void deleteTodo(Long id);

}
