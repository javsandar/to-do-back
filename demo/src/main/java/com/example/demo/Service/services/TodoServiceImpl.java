package com.example.demo.Service.services;

import com.example.demo.Repository.models.TodoEntityFilter;
import com.example.demo.Repository.repositories.TodoRepository;
import com.example.demo.Service.helpers.uuidHelper;
import com.example.demo.Service.models.Todo;
import com.example.demo.Service.models.TodoCreation;
import com.example.demo.Service.models.TodoFilter;
import com.example.demo.Service.models.TodoUpdate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class TodoServiceImpl implements TodoService {
    @Autowired
    private TodoRepository todoRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    @Override
    public List<Todo> getTodosByFilter(TodoFilter filter) {
        TodoEntityFilter todoEntityFilter = modelMapper.map(filter, TodoEntityFilter.class);
        List<Todo> result = todoRepository.findTodosByFilter(todoEntityFilter);
        return result;
    }

    @Override
    public Todo addTodo(TodoCreation todoCreation) {
        Todo todo = modelMapper.map(todoCreation, Todo.class);
        todo.setId(uuidHelper.generateRandomUUID());
        todo.setCreationDate(LocalDate.now());
        Todo result = todoRepository.save(todo);
        return result;
    }

    @Override
    public Todo getTodo(UUID id) {
        Todo result = todoRepository.findById(id).orElse(null);
        if (result == null) {
            return null;
        }
        return result;
    }

    @Override
    public Todo updateTodo(UUID id, TodoUpdate todoUpdate) {
        Todo updatedTodo = todoRepository.findById(id).orElse(null);
        if (updatedTodo == null) {
            return null;
        }
        updatedTodo.setText(todoUpdate.getText());
        updatedTodo.setFinished(todoUpdate.isFinished());
        updatedTodo.setExpireDate(todoUpdate.getExpireDate());
        Todo result = todoRepository.save(updatedTodo);
        return result;
    }

}
