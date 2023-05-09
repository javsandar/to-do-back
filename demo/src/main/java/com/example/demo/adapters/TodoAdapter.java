package com.example.demo.adapters;

import com.example.demo.models.Todo;
import com.example.demo.repositories.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;
@Service
public class TodoAdapter {
    @Autowired
    private TodoRepository todoRepository;

    public Todo add(Todo todo) {
        todoRepository.getTodos().add(todo);
        return todo;
    }

    public Todo update(UUID id) {
        Todo todo = todoRepository.getTodos().stream().filter(t -> t.getId().equals(id)).findFirst().get();
        todo.setFinished(!todo.isFinished());
        return todo;
    }
}
