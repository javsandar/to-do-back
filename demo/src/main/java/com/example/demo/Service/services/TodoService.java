package com.example.demo.Service.services;

import com.example.demo.Controller.models.TodoFilter;
import com.example.demo.Controller.models.TodoCreationRequest;
import com.example.demo.Controller.models.TodoUpdateRequest;
import com.example.demo.Repository.entities.TodoEntity;

import java.util.List;
import java.util.UUID;


public interface TodoService {
    List<TodoEntity> getTodosByFilter(TodoFilter filter);

    List<TodoEntity> getAllTodos();

    TodoEntity addTodo(TodoCreationRequest todoCreationRequest);

    TodoEntity getTodo(UUID id);

    TodoEntity updateTodo(UUID id, TodoUpdateRequest todoUpdateRequest);

}
