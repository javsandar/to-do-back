package com.example.demo.Service.services;

import com.example.demo.Service.models.TodoCreationDto;
import com.example.demo.Service.models.TodoDto;
import com.example.demo.Service.models.TodoFilterDto;
import com.example.demo.Service.models.TodoUpdateDto;

import java.util.List;
import java.util.UUID;


public interface TodoService {
    List<TodoDto> getTodosByFilter(TodoFilterDto todoFilterDto);

    List<TodoDto> getAllTodos();

    TodoDto addTodo(TodoCreationDto todoCreationDto);

    TodoDto getTodo(UUID id);

    TodoDto updateTodo(UUID id, TodoUpdateDto todoUpdateDto);

}
