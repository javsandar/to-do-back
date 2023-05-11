package com.example.demo.Controller.controllers;

import com.example.demo.Controller.models.TodoPutRequest;
import com.example.demo.Controller.models.TodoRequest;
import com.example.demo.Repository.entities.TodoEntity;
import com.example.demo.Service.service.TodoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
public class TodoController {

    @Autowired
    private TodoService todoService;

    @GetMapping("/todos")
    public List<TodoEntity> getTodos(@RequestParam(required = false) Boolean finished) {
        return todoService.todosHandler(finished);
    }

    @PostMapping("/todos")
    public TodoEntity addTodo(@RequestBody @Valid TodoRequest todoRequest) {
        return todoService.addTodo(todoRequest);
    }

    @GetMapping("/todos/{id}")
    public Optional<TodoEntity> getTodo(@PathVariable UUID id) {
        return todoService.getTodo(id);
    }


    @PutMapping("/todos/{id}")
    public Optional<TodoEntity> updateTodo(@RequestBody @Valid TodoPutRequest todoRequest, @PathVariable UUID id) {
        return Optional.ofNullable(todoService.updateTodo(id, todoRequest));
    }

}
