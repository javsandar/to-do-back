package com.example.demo.Service.service;

import com.example.demo.Controller.models.TodoUpdateRequestModel;
import com.example.demo.Controller.models.TodoRequestModel;
import com.example.demo.Repository.entities.TodoEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TodoService {
    List<TodoEntity> todosHandler(Boolean finished);
    List<TodoEntity> getAllTodos();
    List<TodoEntity> getRemainingTodos(Boolean finished);
    TodoEntity addTodo(TodoRequestModel todoRequestModel);
    Optional<TodoEntity> getTodo(UUID id);
    Optional<TodoEntity> updateTodo(UUID id, TodoUpdateRequestModel todoUpdateRequestModel);

}
