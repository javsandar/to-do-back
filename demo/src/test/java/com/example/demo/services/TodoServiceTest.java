package com.example.demo.services;

import com.example.demo.Controller.models.TodoRequestModel;
import com.example.demo.Controller.models.TodoUpdateRequestModel;
import com.example.demo.Repository.entities.TodoEntity;
import com.example.demo.Repository.repositories.TodoRepository;
import com.example.demo.Service.helpers.UUIDhelper;
import com.example.demo.Service.service.TodoService;
import com.example.demo.Service.service.TodoServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

public class TodoServiceTest {

    @InjectMocks
    private TodoService todoService = new TodoServiceImpl();
    @Mock
    private TodoRepository todoRepository;
    private List<TodoEntity> todos;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);

        todos = new ArrayList<>(Arrays.asList(
                new TodoEntity(UUIDhelper.generateRandomUUID(), "Tarea de prueba 1", false),
                new TodoEntity(UUIDhelper.generateRandomUUID(), "Tarea de prueba 2", false),
                new TodoEntity(UUIDhelper.generateRandomUUID(), "Tarea de prueba 3", true)));
    }

    @Test
    public void givenTodosList_whenGetAllTodos_thenListAllTodos() {
        when(todoRepository.findAll()).thenReturn(todos);
        List<TodoEntity> expectedList = todoService.getAllTodos();

        assert (expectedList.containsAll(todos));
    }

    @Test
    public void givenTodosList_whenGetRemainingTodos_thenListRemainingTodos() {
        todos = todos.stream().filter(todo -> !todo.isFinished()).toList();

        when(todoRepository.findByIsFinished(false)).thenReturn(todos);
        List<TodoEntity> expectedList = todoService.getRemainingTodos(false);

        assert (expectedList.containsAll(todos));
    }

    @Test
    public void givenTodoRequestModel_whenAddTodo_thenTodoIsCreated() {
        TodoRequestModel todoRequestModel = new TodoRequestModel("Tarea de prueba 1", false);

        ArgumentCaptor<TodoEntity> todoEntityCaptor = ArgumentCaptor.forClass(TodoEntity.class);
        when(todoRepository.save(todoEntityCaptor.capture())).thenReturn(new TodoEntity());

        todoService.addTodo(todoRequestModel);
        TodoEntity capturedTodoEntity = todoEntityCaptor.getValue();

        Assertions.assertNotNull(capturedTodoEntity.getId());
        Assertions.assertEquals(capturedTodoEntity.getText(), todoRequestModel.getText());
        Assertions.assertEquals(capturedTodoEntity.isFinished(), todoRequestModel.isFinished());

    }

    @Test
    public void givenIdParam_whenGetTodo_thenReturnTodo() {
        UUID id = UUIDhelper.generateRandomUUID();
        Optional<TodoEntity> expectedTodoEntity = Optional.of(new TodoEntity(id, "Tarea de prueba 1", false));

        when(todoRepository.findById(id)).thenReturn(expectedTodoEntity);
        Optional<TodoEntity> returnedTodo = todoService.getTodo(id);

        Assertions.assertEquals(expectedTodoEntity, returnedTodo);

    }

    @Test
    public void givenIdParam_whenUpdateTodo_thenUpdateTodoFields() {
        TodoEntity prevTodo = new TodoEntity(UUIDhelper.generateRandomUUID(), "Tarea de prueba 1", false);
        TodoUpdateRequestModel todoUpdateRequestModel = new TodoUpdateRequestModel("Tarea de prueba actualizada 1", true);
        TodoEntity expectedTodo = new TodoEntity(prevTodo.getId(), todoUpdateRequestModel.getText(), todoUpdateRequestModel.isFinished());

        when(todoRepository.findById(prevTodo.getId())).thenReturn(Optional.of(prevTodo));
        when(todoRepository.save(expectedTodo)).thenReturn(expectedTodo);

        Optional<TodoEntity> updatedTodo = Optional.of(todoService.updateTodo(prevTodo.getId(), todoUpdateRequestModel).get());

        assertEquals(updatedTodo.get().getId(), expectedTodo.getId());
        assertEquals(updatedTodo.get().getText(), expectedTodo.getText());
        assertEquals(updatedTodo.get().isFinished(), expectedTodo.isFinished());
    }

}
