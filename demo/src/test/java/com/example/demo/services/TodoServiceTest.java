package com.example.demo.services;

import com.example.demo.Service.helpers.UUIDhelper;
import com.example.demo.Service.service.TodoService;
import com.example.demo.Controller.models.Todo;
import com.example.demo.Controller.models.TodoRequest;
import com.example.demo.Repository.repositories.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)

public class TodoServiceTest {

    @InjectMocks
    private TodoService todoService;
    @Mock
    private TodoRepository todoRepository;

    @Mock
    private TodoAdapter todoAdapter;

    private List<Todo> todos;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);

        todos = new ArrayList<>(Arrays.asList(
                new Todo(UUIDhelper.generateRandomUUID(), "Tarea de prueba 0", false),
                new Todo(UUIDhelper.generateRandomUUID(), "Tarea de prueba 1", false),
                new Todo(UUIDhelper.generateRandomUUID(), "Tarea de prueba 2", true)));
    }

    @Test
    public void givenTodosList_whenGetAllTodos_thenListAllTodos() {
        when(todoService.getAllTodos()).thenReturn(todos);
        List<Todo> todosList = todoService.getAllTodos();
        assert (todosList.containsAll(todos));
    }

    @Test
    public void givenTodosList_whenGetRemainingTodos_thenListRemainingTodos() {
        todos = todos.stream().filter(todo -> !todo.isFinished()).toList();
        when(todoService.getRemainingTodos()).thenReturn(todos);
        List<Todo> remainingTodosList = todoService.getRemainingTodos();
        assert (remainingTodosList.containsAll(todos));
    }

    @Test
    public void givenNewTodo_whenAddTodo_thenTodoIsAddedToList() {
        TodoRequest newTodoRequest = new TodoRequest("Tarea de prueba 1", false);

        todoService.addTodo(newTodoRequest);
        ArgumentCaptor<Todo> todoCaptor = ArgumentCaptor.forClass(Todo.class);
        verify(todoAdapter).add(todoCaptor.capture());

        Todo newTodo = todoCaptor.getValue();
        assertEquals(newTodo.getText(), newTodoRequest.getText());
        assertEquals(newTodo.isFinished(), newTodoRequest.isFinished());
        assertNotNull(newTodo.getId());

    }

    @Test
    public void givenIdParam_whenGetTodo_thenListTodo() {
        Todo todoExpected = todos.get(0);
        UUID id = todoExpected.getId();
        when(todoRepository.getTodos()).thenReturn(todos);
        Todo result = todoService.getTodo(id);
        assertEquals(result, todoExpected);
    }

    @Test
    public void givenIdParam_whenUpdateTodoBoolean_thenUpdateIsFinished() {
        Todo todo = todos.get(0);
        Todo expectedTodo = new Todo(todo.getId(), todo.getText(), !todo.isFinished());
        Mockito.lenient().when(todoService.updateTodo(todo.getId())).thenReturn(expectedTodo);
        Todo updatedTodo = todoService.updateTodo(todo.getId());
        assertEquals(updatedTodo.isFinished(), expectedTodo.isFinished());
    }

}
