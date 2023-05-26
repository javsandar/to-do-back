package com.example.demo.Controller.controllers;
import com.example.demo.Service.*;
import com.example.demo.Controller.models.TodoFilter;
import com.example.demo.Controller.models.TodoCreationRequest;
import com.example.demo.Controller.models.TodoResponse;
import com.example.demo.Controller.models.TodoUpdateRequest;
import com.example.demo.Service.services.TodoService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
public class TodoController {

    @Autowired
    private TodoService todoService;
    private final ModelMapper modelMapper = new ModelMapper();

    @GetMapping("/todos")
    public ResponseEntity<List<TodoResponse>> getTodos(@ModelAttribute TodoFilter filter) {
        TodoFilterDTO todoFilterDTO = modelMapper.map(filter, TodoFilterDTO.class);

        return new ResponseEntity<>(todoService.getTodosByFilter(filter), HttpStatus.OK);
    }

    @PostMapping("/todos")
    public ResponseEntity<TodoResponse> addTodo(@RequestBody @Valid TodoCreationRequest todoCreationRequest) {
        return new ResponseEntity<>(todoService.addTodo(todoCreationRequest), HttpStatus.CREATED);
    }

    @GetMapping("/todos/{id}")
    public ResponseEntity<TodoResponse> getTodo(@PathVariable UUID id) {
        TodoEntity result = todoService.getTodo(id);
        if (result == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Todo not found");
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/todos/{id}")
    public ResponseEntity<TodoResponse> updateTodo(@RequestBody @Valid TodoUpdateRequest todoUpdateRequest, @PathVariable UUID id) {
        TodoEntity result = todoService.updateTodo(id, todoUpdateRequest);
        if (result == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Todo not found");
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
