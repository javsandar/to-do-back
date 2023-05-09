package com.example.demo.services;

import com.example.demo.adapters.TodoAdapter;
import com.example.demo.helpers.UUIDhelper;
import com.example.demo.models.Todo;
import com.example.demo.repositories.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
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
        Todo newTodo = new Todo("", false);
        when(todoAdapter.add(newTodo)).thenReturn(newTodo);
        Todo result = todoService.addTodo(newTodo);
        then(todoAdapter).should(times(1)).add(newTodo);
        assertEquals(result, newTodo);
        assertNotNull(newTodo.getId());
    }

    @Test
    public void givenId_whenGetTodo_thenListTodo() {
        Todo todoExpected = todos.get(0);
        UUID id = todoExpected.getId();
        when(todoRepository.getTodos()).thenReturn(todos);
        Todo result = todoService.getTodo(id);
        assertEquals(result, todoExpected);
    }

}
