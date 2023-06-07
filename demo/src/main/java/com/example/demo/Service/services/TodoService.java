package com.example.demo.Service.services;

import com.example.demo.Service.models.TodoCreation;
import com.example.demo.Service.models.Todo;
import com.example.demo.Service.models.TodoFilter;
import com.example.demo.Service.models.TodoUpdate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface TodoService {
    List<Todo> getTodosByFilter(TodoFilter todoFilter);

    Todo addTodo(TodoCreation todoCreation);

    Todo getTodo(UUID id);

    Todo updateTodo(UUID id, TodoUpdate todoUpdate);

}
