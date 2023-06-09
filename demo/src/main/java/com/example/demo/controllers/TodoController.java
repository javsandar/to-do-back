package com.example.demo.controllers;

import com.example.demo.controllers.models.TodoCreationRequest;
import com.example.demo.controllers.models.TodoFilterRequest;
import com.example.demo.controllers.models.TodoResponse;
import com.example.demo.controllers.models.TodoUpdateRequest;
import com.example.demo.services.models.TodoCreation;
import com.example.demo.services.models.Todo;
import com.example.demo.services.models.TodoFilter;
import com.example.demo.services.models.TodoUpdate;
import com.example.demo.services.TodoService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
public class TodoController {

    @Autowired
    private TodoService todoService;

    private final ModelMapper modelMapper = new ModelMapper();

    @GetMapping("/todos")
    public ResponseEntity<List<TodoResponse>> getTodos(@ModelAttribute("TodoFilter") TodoFilterRequest filter) {
        TodoFilter todoFilter = modelMapper.map(filter, TodoFilter.class);
        List<Todo> responseDto = todoService.getTodosByFilter(todoFilter);
        List<TodoResponse> response = responseDto.stream().map(element -> modelMapper.map(element, TodoResponse.class)).collect(Collectors.toList());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/todos")
    public ResponseEntity<TodoResponse> addTodo(@RequestBody @Valid TodoCreationRequest todoCreationRequest) {
        TodoCreation todoCreation = modelMapper.map(todoCreationRequest, TodoCreation.class);
        TodoResponse response = modelMapper.map(todoService.addTodo(todoCreation), TodoResponse.class);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/todos/{id}")
    public ResponseEntity<TodoResponse> getTodo(@PathVariable UUID id) {
        Todo responseDto = todoService.getTodo(id);
        if (responseDto == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Todo not found");
        }
        TodoResponse response = modelMapper.map(responseDto, TodoResponse.class);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/todos/{id}")
    public ResponseEntity<TodoResponse> updateTodo(@RequestBody @Valid TodoUpdateRequest todoUpdateRequest, @PathVariable UUID id) {
        TodoUpdate todoUpdate = modelMapper.map(todoUpdateRequest, TodoUpdate.class);
        Todo responseDto = todoService.updateTodo(id, todoUpdate);
        if (responseDto == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Todo not found");
        }
        TodoResponse response = modelMapper.map(responseDto, TodoResponse.class);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
