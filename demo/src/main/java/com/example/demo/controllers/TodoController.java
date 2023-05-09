package com.example.demo.controllers;

import com.example.demo.models.Todo;
import com.example.demo.services.TodoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class TodoController {

    @Autowired
    private TodoService todoService;

    @GetMapping("/todos")
    public List<Todo> getTodos(@RequestParam(required = false) Boolean finished) {
        return todoService.todosHandler(finished);
    }

    @PostMapping("/todos")
    public Todo addTodo(@RequestBody @Valid Todo todo) {
        return todoService.addTodo(todo);
    }

    @GetMapping("/todos/{id}")
    public Todo getTodo(@PathVariable UUID id) {
        return todoService.getTodo(id);
    }


    @PutMapping("/todos/{id}")
     public Todo updateTodo(@RequestBody @Valid Todo todo, @PathVariable UUID id) {
       return todoService.updateTodoBoolean(id);
    }
//    @DeleteMapping("/todos/{id}")
//    public void deleteTodo(@PathVariable int id) {
//        todoService.deleteTodo(id);
//     }

}
