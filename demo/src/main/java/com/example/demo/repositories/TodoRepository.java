package com.example.demo.repositories;

import com.example.demo.repositories.models.TodoEntityFilter;
import com.example.demo.services.models.Todo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TodoRepository  {
    List<Todo> findTodosByFilter(TodoEntityFilter todoEntityFilter);
    Todo save(Todo todo);
    Optional<Todo> findById(UUID id);
}
