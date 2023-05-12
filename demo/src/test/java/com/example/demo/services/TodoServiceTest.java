package com.example.demo.services;

import com.example.demo.Repository.entities.TodoEntity;
import com.example.demo.Service.helpers.UUIDhelper;
import com.example.demo.Service.service.TodoService;
import com.example.demo.Controller.models.TodoRequestModel;
import com.example.demo.Repository.repositories.TodoRepository;
import com.example.demo.Service.service.TodoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)

public class TodoServiceTest {

    @InjectMocks
    private TodoService todoService = new TodoServiceImpl();
    @Mock
    private TodoRepository todoRepository;
    private List<TodoEntity> todos;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);

        todos = new ArrayList<>(Arrays.asList(
                new TodoEntity(UUIDhelper.generateRandomUUID(), "Tarea de prueba 0", false),
                new TodoEntity(UUIDhelper.generateRandomUUID(), "Tarea de prueba 1", false),
                new TodoEntity(UUIDhelper.generateRandomUUID(), "Tarea de prueba 2", true)));
    }

    @Test
    public void givenTodosList_whenGetAllTodos_thenListAllTodos() {
        when(todoRepository.findAll()).thenReturn(todos);
        List<TodoEntity> expectedList = todoService.getAllTodos();
        assert(expectedList.containsAll(todos));
    }

//    @Test
//    public void givenTodosList_whenGetRemainingTodos_thenListRemainingTodos() {
//        todos = todos.stream().filter(todo -> !todo.isFinished()).toList();
//        when(todoServiceImpl.getRemainingTodos()).thenReturn(todos);
//        List<Todo> remainingTodosList = todoServiceImpl.getRemainingTodos();
//        assert (remainingTodosList.containsAll(todos));
//    }
//
//    @Test
//    public void givenNewTodo_whenAddTodo_thenTodoIsAddedToList() {
//        TodoRequestModel newTodoRequestModel = new TodoRequestModel("Tarea de prueba 1", false);
//
//        todoServiceImpl.addTodo(newTodoRequestModel);
//        ArgumentCaptor<Todo> todoCaptor = ArgumentCaptor.forClass(Todo.class);
//        verify(todoAdapter).add(todoCaptor.capture());
//
//        Todo newTodo = todoCaptor.getValue();
//        assertEquals(newTodo.getText(), newTodoRequestModel.getText());
//        assertEquals(newTodo.isFinished(), newTodoRequestModel.isFinished());
//        assertNotNull(newTodo.getId());
//
//    }
//
//    @Test
//    public void givenIdParam_whenGetTodo_thenListTodo() {
//        Todo todoExpected = todos.get(0);
//        UUID id = todoExpected.getId();
//        when(todoRepository.getTodos()).thenReturn(todos);
//        Todo result = todoServiceImpl.getTodo(id);
//        assertEquals(result, todoExpected);
//    }
//
//    @Test
//    public void givenIdParam_whenUpdateTodoBoolean_thenUpdateIsFinished() {
//        Todo todo = todos.get(0);
//        Todo expectedTodo = new Todo(todo.getId(), todo.getText(), !todo.isFinished());
//        Mockito.lenient().when(todoServiceImpl.updateTodo(todo.getId())).thenReturn(expectedTodo);
//        Todo updatedTodo = todoServiceImpl.updateTodo(todo.getId());
//        assertEquals(updatedTodo.isFinished(), expectedTodo.isFinished());
//    }

}
