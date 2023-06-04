package com.example.demo.Service.services;

import com.example.demo.Repository.entities.TodoEntity;
import com.example.demo.Repository.models.TodoEntityFilter;
import com.example.demo.Repository.repositories.TodoRepository;
import com.example.demo.Service.helpers.uuidHelper;
import com.example.demo.Service.models.TodoCreationDto;
import com.example.demo.Service.models.TodoDto;
import com.example.demo.Service.models.TodoFilterDto;
import com.example.demo.Service.models.TodoUpdateDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class TodoServiceImpl implements TodoService {
    @Autowired
    private TodoRepository todoRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    @Override
    public List<TodoDto> getTodosByFilter(TodoFilterDto filter) {
        TodoEntityFilter todoEntityFilter = modelMapper.map(filter, TodoEntityFilter.class);
        List<TodoEntity> result = todoRepository.findTodosByFilter(todoEntityFilter);
        List<TodoDto> resultDto = result.stream().map(element -> modelMapper.map(element, TodoDto.class)).toList();
        return resultDto;
    }

    @Override
    public TodoDto addTodo(TodoCreationDto todoCreationDto) {
        TodoEntity todoEntity = modelMapper.map(todoCreationDto, TodoEntity.class);
        todoEntity.setId(uuidHelper.generateRandomUUID());
        todoEntity.setCreationDate(LocalDate.now());
        TodoDto resultDto = modelMapper.map(todoRepository.save(todoEntity), TodoDto.class);
        return resultDto;
    }

    @Override
    public TodoDto getTodo(UUID id) {
        TodoEntity result = todoRepository.findById(id).orElse(null);
        if (result == null){
            return null;
        }
        TodoDto resultDto = modelMapper.map(result, TodoDto.class);
        return resultDto;
    }

    @Override
    public TodoDto updateTodo(UUID id, TodoUpdateDto todoUpdateDto) {
        TodoEntity updatedTodo = todoRepository.findById(id).orElse(null);
        if (updatedTodo == null) {
            return null;
        }
        updatedTodo.setText(todoUpdateDto.getText());
        updatedTodo.setFinished(todoUpdateDto.isFinished());
        updatedTodo.setExpireDate(todoUpdateDto.getExpireDate());
        TodoEntity result = todoRepository.save(updatedTodo);
        TodoDto resultDto = modelMapper.map(result, TodoDto.class);
        return resultDto;
    }

}
