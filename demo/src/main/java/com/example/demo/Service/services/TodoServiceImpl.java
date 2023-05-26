package com.example.demo.Service.services;

import com.example.demo.Repository.entities.TodoEntity;
import com.example.demo.Repository.repositories.TodoRepository;
import com.example.demo.Service.helpers.UUIDhelper;
import com.example.demo.Service.models.TodoCreationDto;
import com.example.demo.Service.models.TodoDto;
import com.example.demo.Service.models.TodoFilterDto;
import com.example.demo.Service.models.TodoUpdateDto;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper = new ModelMapper();

    @Override
    public List<TodoDto> getTodosByFilter(TodoFilterDto filter) {
        System.out.println(filter.toString());
        if (filter.getFinished() == null && filter.getCreationDate() == null && filter.getExpireDate() == null) {
            return getAllTodos();
        }
        TodoEntity exampleTodo = new TodoEntity(null, null, filter.getFinished(), filter.getCreationDate(), filter.getExpireDate());
        Example<TodoEntity> example = Example.of(exampleTodo);
        List<TodoEntity> result = todoRepository.findAll(example);
        List<TodoDto> resultDto = result.stream().map(element -> modelMapper.map(element, TodoDto.class)).toList();
        return resultDto;
    }

    @Override
    public List<TodoDto> getAllTodos() {
        List<TodoEntity> result = todoRepository.findAll();
        List<TodoDto> resultDto = result.stream().map(element -> modelMapper.map(element, TodoDto.class)).toList();
        return resultDto;
    }

    @Override
    public TodoDto addTodo(TodoCreationDto todoCreationDto) {
        TodoEntity todoEntity = modelMapper.map(todoCreationDto, TodoEntity.class);
        todoEntity.setId(UUIDhelper.generateRandomUUID());
        todoEntity.setCreationDate(LocalDate.now());
        TodoDto resultDto = modelMapper.map(todoRepository.save(todoEntity), TodoDto.class);
        return resultDto;
    }

    @Override
    public TodoDto getTodo(UUID id) {
        TodoEntity result = todoRepository.findById(id).orElse(null);
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
