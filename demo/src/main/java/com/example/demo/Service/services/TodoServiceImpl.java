package com.example.demo.Service.services;

import com.example.demo.Controller.models.TodoFilter;
import com.example.demo.Controller.models.TodoCreationRequest;
import com.example.demo.Controller.models.TodoUpdateRequest;
import com.example.demo.Repository.entities.TodoEntity;
import com.example.demo.Repository.repositories.TodoRepository;
import com.example.demo.Service.helpers.UUIDhelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class TodoServiceImpl implements TodoService {
    @Autowired
    private TodoRepository todoRepository;

    @Override
    public List<TodoEntity> getTodosByFilter(TodoFilter filter) {
        if(filter.getFinished() == null && filter.getCreationDate() == null && filter.getExpireDate() == null){
            return getAllTodos();
        }
        TodoEntity exampleTodo = new TodoEntity(null, null, filter.getFinished(), filter.getCreationDate(), filter.getExpireDate());
        Example<TodoEntity> example = Example.of(exampleTodo);
        return todoRepository.findAll(example);
    }

    @Override
    public List<TodoEntity> getAllTodos() {
        return todoRepository.findAll();
    }

    @Override
    public TodoEntity addTodo(TodoCreationRequest todoCreationRequest) {
        TodoEntity todoEntity = new TodoEntity(UUIDhelper.generateRandomUUID(), todoCreationRequest.getText(), todoCreationRequest.isFinished(), LocalDate.now(), todoCreationRequest.getExpireDate());
        return todoRepository.save(todoEntity);
    }

    @Override
    public TodoEntity getTodo(UUID id) {
        return todoRepository.findById(id).orElse(null);
    }

    @Override
    public TodoEntity updateTodo(UUID id, TodoUpdateRequest todoUpdateRequest) {
        TodoEntity updatedTodo = todoRepository.findById(id).orElse(null);
        if (updatedTodo == null) {
            return null;
        }
        updatedTodo.setText(todoUpdateRequest.getText());
        updatedTodo.setFinished(todoUpdateRequest.isFinished());
        updatedTodo.setExpireDate(todoUpdateRequest.getExpireDate());
        return todoRepository.save(updatedTodo);
    }

}
