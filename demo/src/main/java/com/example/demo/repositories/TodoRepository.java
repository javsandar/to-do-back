package com.example.demo.repositories;

import com.example.demo.models.Todo;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;


@Repository
public class TodoRepository {
    private List<Todo> todos = new ArrayList<>();

    public List<Todo> getTodos() {
        return todos;
    }

    public void setTodos(List<Todo> todos) {
        this.todos = todos;
    }

    public Todo add(Todo todo) {
        getTodos().add(todo);
        return todo;
    }
}
