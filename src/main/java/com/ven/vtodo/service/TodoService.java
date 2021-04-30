package com.ven.vtodo.service;

import com.ven.vtodo.po.Todo;
import com.ven.vtodo.po.User;

import java.util.Date;
import java.util.List;

public interface TodoService {

    Todo saveTodo(Todo todo);

    void saveFinishedTodo(Todo todo);

    Todo getTodo(Long id);

    List<Todo> listTodo();

    List<Todo> listUnfinishedTodosByDateAndUser(Date date, User user);

    List<Todo> listFinishedTodosByDateAndUser(Date date, User user);

    List<Todo> listUnfinishedTodosByOtherDateAndUser(Date date, User user);

    void deleteTodo(Long id);

}
