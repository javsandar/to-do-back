package com.example.demo.Controller.controllers;

import com.example.demo.Controller.models.TodoCreationRequest;
import com.example.demo.Controller.models.TodoFilter;
import com.example.demo.Controller.models.TodoResponse;
import com.example.demo.Controller.models.TodoUpdateRequest;
import com.example.demo.Service.models.TodoCreationDto;
import com.example.demo.Service.models.TodoDto;
import com.example.demo.Service.models.TodoFilterDto;
import com.example.demo.Service.models.TodoUpdateDto;
import com.example.demo.Service.services.TodoService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
public class TodoController {

    @Autowired
    private TodoService todoService;

    private final ModelMapper modelMapper = new ModelMapper();

    @GetMapping("/todos")
    public ResponseEntity<List<TodoResponse>> getTodos(@ModelAttribute("TodoFilter") TodoFilter filter) {
        TodoFilterDto todoFilterDto = modelMapper.map(filter, TodoFilterDto.class);
        List<TodoDto> responseDto = todoService.getTodosByFilter(todoFilterDto);
        List<TodoResponse> response = responseDto.stream().map(element -> modelMapper.map(element, TodoResponse.class)).collect(Collectors.toList());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/todos")
    public ResponseEntity<TodoResponse> addTodo(@RequestBody @Valid TodoCreationRequest todoCreationRequest) {
        TodoCreationDto todoCreationDto = modelMapper.map(todoCreationRequest, TodoCreationDto.class);
        TodoResponse response = modelMapper.map(todoService.addTodo(todoCreationDto), TodoResponse.class);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/todos/{id}")
    public ResponseEntity<TodoResponse> getTodo(@PathVariable UUID id) {
        TodoDto responseDto = todoService.getTodo(id);
        if (responseDto == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Todo not found");
        }
        TodoResponse response = modelMapper.map(responseDto, TodoResponse.class);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/todos/{id}")
    public ResponseEntity<TodoResponse> updateTodo(@RequestBody @Valid TodoUpdateRequest todoUpdateRequest, @PathVariable UUID id) {
        TodoUpdateDto todoUpdateDto = modelMapper.map(todoUpdateRequest, TodoUpdateDto.class);
        TodoDto responseDto = todoService.updateTodo(id, todoUpdateDto);
        if (responseDto == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Todo not found");
        }
        TodoResponse response = modelMapper.map(responseDto, TodoResponse.class);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
