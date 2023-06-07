package com.example.demo.Repository.repositories;

import com.example.demo.Repository.models.TodoEntityFilter;
import com.example.demo.Service.models.Todo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TodoRepository  {
    List<Todo> findTodosByFilter(TodoEntityFilter todoEntityFilter);
    Todo save(Todo todo);
    Optional<Todo> findById(UUID id);
}
