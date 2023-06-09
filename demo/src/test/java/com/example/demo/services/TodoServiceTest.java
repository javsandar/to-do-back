package com.example.demo.services;

import com.example.demo.repositories.TodoRepository;
import com.example.demo.repositories.models.TodoEntityFilter;
import com.example.demo.services.helpers.uuidHelper;
import com.example.demo.services.models.Todo;
import com.example.demo.services.models.TodoCreation;
import com.example.demo.services.models.TodoFilter;
import com.example.demo.services.models.TodoUpdate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TodoServiceTest {
    @Mock
    private TodoRepository todoRepository;
    @InjectMocks
    private TodoService todoService = new TodoServiceImpl();

    @Autowired
    private ModelMapper modelMapper = new ModelMapper();

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @ParameterizedTest
    @MethodSource("todosProvider")
    @DisplayName("GetTodosByFilter")
    public void givenTodoFilter_whenGetTodosByFilter_thenReturnFilteredTodos(TodoFilter todoFilter, List<Todo> providedTodos) {
        //given
        //when
        when(todoRepository.findTodosByFilter(any(TodoEntityFilter.class))).thenReturn(providedTodos);
        //then
        List<Todo> result = todoService.getTodosByFilter(todoFilter);
        verify(todoRepository, times(1)).findTodosByFilter(any(TodoEntityFilter.class));
        Assertions.assertEquals(result, providedTodos);
    }

    private static Stream<Arguments> todosProvider() {
        TodoFilter filterAllNull = new TodoFilter(null, null, null);
        TodoFilter filterFinishedFalse = new TodoFilter(false, null, null);
        TodoFilter filterExpireDate = new TodoFilter(null, null, Collections.singletonList("2023-06-01"));
        TodoFilter filterFinishedAndExpireDate = new TodoFilter(true, null, Collections.singletonList("2023-06-06"));

        TodoFilter filterGteExpireDate = new TodoFilter(null, null, Collections.singletonList("gte:2023-06-01"));
        TodoFilter filterGtExpireDate = new TodoFilter(null, null, Collections.singletonList("gt:2023-06-01"));
        TodoFilter filterLteExpireDate = new TodoFilter(null, null, Collections.singletonList("lte:2023-06-10"));
        TodoFilter filterLtExpireDate = new TodoFilter(null, null, Collections.singletonList("lt:2023-06-10"));

        TodoFilter filterGteLteExpireDate = new TodoFilter(null, null, Arrays.asList("gte:2023-06-01", "lte:2023-06-10"));
        TodoFilter filterGteLtExpireDate = new TodoFilter(null, null, Arrays.asList("gte:2023-06-01", "lt:2023-06-10"));
        TodoFilter filterGtLteExpireDate = new TodoFilter(null, null, Arrays.asList("gt:2023-06-01", "lte:2023-06-10"));
        TodoFilter filterGtLtExpireDate = new TodoFilter(null, null, Arrays.asList("gt:2023-06-01", "lt:2023-06-10"));

        List<Todo> listAllNull = Arrays.asList(new Todo(UUID.fromString("af690230-84f3-45ea-a960-fc3d4bc99f65"), "Tarea de prueba 1", false, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-01")), new Todo(UUID.fromString("8daccd58-6131-45d4-8a6d-9e14931598bc"), "Tarea de prueba 2", false, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-02")), new Todo(UUID.fromString("b967e9b5-29eb-4b82-84a0-14c06c700e70"), "Tarea de prueba 3", false, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-03")), new Todo(UUID.fromString("b5806ced-08b5-4019-9785-458a0aecd820"), "Tarea de prueba 4", false, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-04")), new Todo(UUID.fromString("6c9f6cb9-1615-4be5-ae97-6b536e23942f"), "Tarea de prueba 5", false, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-05")), new Todo(UUID.fromString("bca8a701-9dfe-4630-8523-6865678eeefb"), "Tarea de prueba 6", true, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-06")), new Todo(UUID.fromString("86e4f1f4-3741-4b52-bf9c-efdd2ca1eaf1"), "Tarea de prueba 7", true, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-07")), new Todo(UUID.fromString("221ec078-bf3f-45cd-8b5a-6fc594978a15"), "Tarea de prueba 8", true, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-08")), new Todo(UUID.fromString("aca63b33-3946-4161-8286-547f197a3915"), "Tarea de prueba 9", true, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-09")), new Todo(UUID.fromString("31a0200d-d1e7-435d-aee7-c04fae5806a0"), "Tarea de prueba 10", true, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-10")));
        List<Todo> listFinishedFalse = Arrays.asList(new Todo(UUID.fromString("af690230-84f3-45ea-a960-fc3d4bc99f65"), "Tarea de prueba 1", false, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-01")), new Todo(UUID.fromString("8daccd58-6131-45d4-8a6d-9e14931598bc"), "Tarea de prueba 2", false, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-02")), new Todo(UUID.fromString("b967e9b5-29eb-4b82-84a0-14c06c700e70"), "Tarea de prueba 3", false, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-03")), new Todo(UUID.fromString("b5806ced-08b5-4019-9785-458a0aecd820"), "Tarea de prueba 4", false, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-04")), new Todo(UUID.fromString("6c9f6cb9-1615-4be5-ae97-6b536e23942f"), "Tarea de prueba 5", false, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-05")));
        List<Todo> listExpireDate = Arrays.asList(new Todo(UUID.fromString("af690230-84f3-45ea-a960-fc3d4bc99f65"), "Tarea de prueba 1", false, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-01")));
        List<Todo> listFinishedAndExpireDate = Arrays.asList(new Todo(UUID.fromString("bca8a701-9dfe-4630-8523-6865678eeefb"), "Tarea de prueba 6", true, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-06")));

        List<Todo> listGteExpireDate = Arrays.asList(new Todo(UUID.fromString("af690230-84f3-45ea-a960-fc3d4bc99f65"), "Tarea de prueba 1", false, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-01")), new Todo(UUID.fromString("8daccd58-6131-45d4-8a6d-9e14931598bc"), "Tarea de prueba 2", false, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-02")), new Todo(UUID.fromString("b967e9b5-29eb-4b82-84a0-14c06c700e70"), "Tarea de prueba 3", false, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-03")), new Todo(UUID.fromString("b5806ced-08b5-4019-9785-458a0aecd820"), "Tarea de prueba 4", false, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-04")), new Todo(UUID.fromString("6c9f6cb9-1615-4be5-ae97-6b536e23942f"), "Tarea de prueba 5", false, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-05")), new Todo(UUID.fromString("bca8a701-9dfe-4630-8523-6865678eeefb"), "Tarea de prueba 6", true, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-06")), new Todo(UUID.fromString("86e4f1f4-3741-4b52-bf9c-efdd2ca1eaf1"), "Tarea de prueba 7", true, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-07")), new Todo(UUID.fromString("221ec078-bf3f-45cd-8b5a-6fc594978a15"), "Tarea de prueba 8", true, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-08")), new Todo(UUID.fromString("aca63b33-3946-4161-8286-547f197a3915"), "Tarea de prueba 9", true, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-09")), new Todo(UUID.fromString("31a0200d-d1e7-435d-aee7-c04fae5806a0"), "Tarea de prueba 10", true, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-10")));
        List<Todo> listGtExpireDate = Arrays.asList(new Todo(UUID.fromString("8daccd58-6131-45d4-8a6d-9e14931598bc"), "Tarea de prueba 2", false, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-02")), new Todo(UUID.fromString("b967e9b5-29eb-4b82-84a0-14c06c700e70"), "Tarea de prueba 3", false, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-03")), new Todo(UUID.fromString("b5806ced-08b5-4019-9785-458a0aecd820"), "Tarea de prueba 4", false, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-04")), new Todo(UUID.fromString("6c9f6cb9-1615-4be5-ae97-6b536e23942f"), "Tarea de prueba 5", false, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-05")), new Todo(UUID.fromString("bca8a701-9dfe-4630-8523-6865678eeefb"), "Tarea de prueba 6", true, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-06")), new Todo(UUID.fromString("86e4f1f4-3741-4b52-bf9c-efdd2ca1eaf1"), "Tarea de prueba 7", true, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-07")), new Todo(UUID.fromString("221ec078-bf3f-45cd-8b5a-6fc594978a15"), "Tarea de prueba 8", true, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-08")), new Todo(UUID.fromString("aca63b33-3946-4161-8286-547f197a3915"), "Tarea de prueba 9", true, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-09")), new Todo(UUID.fromString("31a0200d-d1e7-435d-aee7-c04fae5806a0"), "Tarea de prueba 10", true, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-10")));
        List<Todo> listLteExpireDate = Arrays.asList(new Todo(UUID.fromString("af690230-84f3-45ea-a960-fc3d4bc99f65"), "Tarea de prueba 1", false, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-01")), new Todo(UUID.fromString("8daccd58-6131-45d4-8a6d-9e14931598bc"), "Tarea de prueba 2", false, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-02")), new Todo(UUID.fromString("b967e9b5-29eb-4b82-84a0-14c06c700e70"), "Tarea de prueba 3", false, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-03")), new Todo(UUID.fromString("b5806ced-08b5-4019-9785-458a0aecd820"), "Tarea de prueba 4", false, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-04")), new Todo(UUID.fromString("6c9f6cb9-1615-4be5-ae97-6b536e23942f"), "Tarea de prueba 5", false, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-05")), new Todo(UUID.fromString("bca8a701-9dfe-4630-8523-6865678eeefb"), "Tarea de prueba 6", true, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-06")), new Todo(UUID.fromString("86e4f1f4-3741-4b52-bf9c-efdd2ca1eaf1"), "Tarea de prueba 7", true, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-07")), new Todo(UUID.fromString("221ec078-bf3f-45cd-8b5a-6fc594978a15"), "Tarea de prueba 8", true, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-08")), new Todo(UUID.fromString("aca63b33-3946-4161-8286-547f197a3915"), "Tarea de prueba 9", true, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-09")), new Todo(UUID.fromString("31a0200d-d1e7-435d-aee7-c04fae5806a0"), "Tarea de prueba 10", true, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-10")));
        List<Todo> listLtExpireDate = Arrays.asList(new Todo(UUID.fromString("af690230-84f3-45ea-a960-fc3d4bc99f65"), "Tarea de prueba 1", false, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-01")), new Todo(UUID.fromString("8daccd58-6131-45d4-8a6d-9e14931598bc"), "Tarea de prueba 2", false, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-02")), new Todo(UUID.fromString("b967e9b5-29eb-4b82-84a0-14c06c700e70"), "Tarea de prueba 3", false, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-03")), new Todo(UUID.fromString("b5806ced-08b5-4019-9785-458a0aecd820"), "Tarea de prueba 4", false, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-04")), new Todo(UUID.fromString("6c9f6cb9-1615-4be5-ae97-6b536e23942f"), "Tarea de prueba 5", false, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-05")), new Todo(UUID.fromString("bca8a701-9dfe-4630-8523-6865678eeefb"), "Tarea de prueba 6", true, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-06")), new Todo(UUID.fromString("86e4f1f4-3741-4b52-bf9c-efdd2ca1eaf1"), "Tarea de prueba 7", true, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-07")), new Todo(UUID.fromString("221ec078-bf3f-45cd-8b5a-6fc594978a15"), "Tarea de prueba 8", true, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-08")), new Todo(UUID.fromString("aca63b33-3946-4161-8286-547f197a3915"), "Tarea de prueba 9", true, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-09")));

        List<Todo> listGteLteExpireDate = Arrays.asList(new Todo(UUID.fromString("af690230-84f3-45ea-a960-fc3d4bc99f65"), "Tarea de prueba 1", false, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-01")), new Todo(UUID.fromString("8daccd58-6131-45d4-8a6d-9e14931598bc"), "Tarea de prueba 2", false, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-02")), new Todo(UUID.fromString("b967e9b5-29eb-4b82-84a0-14c06c700e70"), "Tarea de prueba 3", false, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-03")), new Todo(UUID.fromString("b5806ced-08b5-4019-9785-458a0aecd820"), "Tarea de prueba 4", false, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-04")), new Todo(UUID.fromString("6c9f6cb9-1615-4be5-ae97-6b536e23942f"), "Tarea de prueba 5", false, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-05")), new Todo(UUID.fromString("bca8a701-9dfe-4630-8523-6865678eeefb"), "Tarea de prueba 6", true, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-06")), new Todo(UUID.fromString("86e4f1f4-3741-4b52-bf9c-efdd2ca1eaf1"), "Tarea de prueba 7", true, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-07")), new Todo(UUID.fromString("221ec078-bf3f-45cd-8b5a-6fc594978a15"), "Tarea de prueba 8", true, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-08")), new Todo(UUID.fromString("aca63b33-3946-4161-8286-547f197a3915"), "Tarea de prueba 9", true, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-09")));
        List<Todo> listGtLteExpireDate = Arrays.asList(new Todo(UUID.fromString("8daccd58-6131-45d4-8a6d-9e14931598bc"), "Tarea de prueba 2", false, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-02")), new Todo(UUID.fromString("b967e9b5-29eb-4b82-84a0-14c06c700e70"), "Tarea de prueba 3", false, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-03")), new Todo(UUID.fromString("b5806ced-08b5-4019-9785-458a0aecd820"), "Tarea de prueba 4", false, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-04")), new Todo(UUID.fromString("6c9f6cb9-1615-4be5-ae97-6b536e23942f"), "Tarea de prueba 5", false, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-05")), new Todo(UUID.fromString("bca8a701-9dfe-4630-8523-6865678eeefb"), "Tarea de prueba 6", true, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-06")), new Todo(UUID.fromString("86e4f1f4-3741-4b52-bf9c-efdd2ca1eaf1"), "Tarea de prueba 7", true, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-07")), new Todo(UUID.fromString("221ec078-bf3f-45cd-8b5a-6fc594978a15"), "Tarea de prueba 8", true, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-08")), new Todo(UUID.fromString("aca63b33-3946-4161-8286-547f197a3915"), "Tarea de prueba 9", true, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-09")));
        List<Todo> listGteLtExpireDate = Arrays.asList(new Todo(UUID.fromString("af690230-84f3-45ea-a960-fc3d4bc99f65"), "Tarea de prueba 1", false, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-01")), new Todo(UUID.fromString("8daccd58-6131-45d4-8a6d-9e14931598bc"), "Tarea de prueba 2", false, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-02")), new Todo(UUID.fromString("b967e9b5-29eb-4b82-84a0-14c06c700e70"), "Tarea de prueba 3", false, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-03")), new Todo(UUID.fromString("b5806ced-08b5-4019-9785-458a0aecd820"), "Tarea de prueba 4", false, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-04")), new Todo(UUID.fromString("6c9f6cb9-1615-4be5-ae97-6b536e23942f"), "Tarea de prueba 5", false, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-05")), new Todo(UUID.fromString("bca8a701-9dfe-4630-8523-6865678eeefb"), "Tarea de prueba 6", true, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-06")), new Todo(UUID.fromString("86e4f1f4-3741-4b52-bf9c-efdd2ca1eaf1"), "Tarea de prueba 7", true, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-07")), new Todo(UUID.fromString("221ec078-bf3f-45cd-8b5a-6fc594978a15"), "Tarea de prueba 8", true, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-08")));
        List<Todo> listGtLtExpireDate = Arrays.asList(new Todo(UUID.fromString("8daccd58-6131-45d4-8a6d-9e14931598bc"), "Tarea de prueba 2", false, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-02")), new Todo(UUID.fromString("b967e9b5-29eb-4b82-84a0-14c06c700e70"), "Tarea de prueba 3", false, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-03")), new Todo(UUID.fromString("b5806ced-08b5-4019-9785-458a0aecd820"), "Tarea de prueba 4", false, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-04")), new Todo(UUID.fromString("6c9f6cb9-1615-4be5-ae97-6b536e23942f"), "Tarea de prueba 5", false, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-05")), new Todo(UUID.fromString("bca8a701-9dfe-4630-8523-6865678eeefb"), "Tarea de prueba 6", true, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-06")), new Todo(UUID.fromString("86e4f1f4-3741-4b52-bf9c-efdd2ca1eaf1"), "Tarea de prueba 7", true, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-07")), new Todo(UUID.fromString("221ec078-bf3f-45cd-8b5a-6fc594978a15"), "Tarea de prueba 8", true, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-08")));

        return Stream.of(Arguments.of(filterAllNull, listAllNull), Arguments.of(filterFinishedFalse, listFinishedFalse), Arguments.of(filterExpireDate, listExpireDate), Arguments.of(filterFinishedAndExpireDate, listFinishedAndExpireDate), Arguments.of(filterGteExpireDate, listGteExpireDate), Arguments.of(filterGtExpireDate, listGtExpireDate), Arguments.of(filterLteExpireDate, listLteExpireDate), Arguments.of(filterLtExpireDate, listLtExpireDate), Arguments.of(filterGteLteExpireDate, listGteLteExpireDate), Arguments.of(filterGteLtExpireDate, listGteLtExpireDate), Arguments.of(filterGtLteExpireDate, listGtLteExpireDate), Arguments.of(filterGtLtExpireDate, listGtLtExpireDate));

    }


    @Test
    @DisplayName("AddTodo")
    public void givenTodoCreation_whenAddTodo_thenTodoIsCreated() {
        //given
        TodoCreation todoCreation = new TodoCreation("Tarea de prueba 1", false, LocalDate.of(2023, 5, 26));
        //when
        Todo expectedTodo = new Todo(uuidHelper.generateRandomUUID(), todoCreation.getText(), todoCreation.isFinished(), LocalDate.now(), todoCreation.getExpireDate());
        when(todoRepository.save(any(Todo.class))).thenReturn(expectedTodo);
        //then
        Todo result = todoService.addTodo(todoCreation);
        Assertions.assertEquals(result, expectedTodo);
    }

    @Test
    @DisplayName("GetTodo")
    public void givenIdParam_whenGetTodo_thenReturnTodo() {
        //given
        UUID id = uuidHelper.generateRandomUUID();
        //when
        Todo expectedTodo = new Todo(id, "Tarea de prueba 1", false, LocalDate.of(2023, 5, 24), LocalDate.of(2023, 5, 25));
        when(todoRepository.findById(id)).thenReturn(Optional.of(expectedTodo));
        //then
        Todo result = (todoService.getTodo(id));
        Assertions.assertEquals(result, expectedTodo);
    }

    @Test
    public void givenTodoUpdate_whenUpdateTodo_thenUpdateTodoFields() {
        //given
        TodoUpdate todoUpdate = new TodoUpdate("Tarea actualizada 1", true, LocalDate.of(2023, 5, 24));
        //when
        Todo expectedTodo = new Todo(uuidHelper.generateRandomUUID(), "Tarea de prueba 1", false, LocalDate.of(2023, 5, 24), LocalDate.of(2023, 5, 25));
        when(todoRepository.findById(expectedTodo.getId())).thenReturn(Optional.of(expectedTodo));
        expectedTodo.setText(todoUpdate.getText());
        expectedTodo.setFinished(todoUpdate.isFinished());
        expectedTodo.setExpireDate(todoUpdate.getExpireDate());
        when(todoRepository.save(expectedTodo)).thenReturn(expectedTodo);
        //then
        Todo result = todoService.updateTodo(expectedTodo.getId(), todoUpdate);
        Assertions.assertEquals(result, expectedTodo);
    }

    /* TESTING CASOS DE ERROR */
    @Test
    public void givenInvalidIdParam_whenGetTodo_thenReturnNull() {
        //given
        UUID invalidId = uuidHelper.generateRandomUUID();
        //when
        when(todoRepository.findById(invalidId)).thenReturn(Optional.empty());
        //then
        Todo result = todoService.getTodo(invalidId);
        assert (result == null);
    }

    @Test
    public void givenInvalidIdParam_whenUpdateTodo_thenReturnNull() {
        //given
        UUID invalidId = uuidHelper.generateRandomUUID();
        TodoUpdate todoUpdate = new TodoUpdate("Tarea actualizada 1", true, LocalDate.of(2023, 5, 24));
        //when
        when(todoRepository.findById(invalidId)).thenReturn(Optional.empty());
        //then
        Todo result = todoService.updateTodo(invalidId, todoUpdate);
        assert (result == null);
    }

}
