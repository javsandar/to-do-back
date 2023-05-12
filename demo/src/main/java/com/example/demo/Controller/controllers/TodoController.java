package com.example.demo.Controller.controllers;

import com.example.demo.Controller.models.TodoUpdateRequestModel;
import com.example.demo.Controller.models.TodoRequestModel;
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
    public TodoEntity addTodo(@RequestBody @Valid TodoRequestModel todoRequestModel) {
        return todoService.addTodo(todoRequestModel);
    }

    @GetMapping("/todos/{id}")
    public Optional<TodoEntity> getTodo(@PathVariable UUID id) {
        return todoService.getTodo(id);
    }


    @PutMapping("/todos/{id}")
    public Optional<TodoEntity> updateTodo(@RequestBody @Valid TodoUpdateRequestModel todoUpdateRequestModel, @PathVariable UUID id) {
        return todoService.updateTodo(id, todoUpdateRequestModel);
    }

}
