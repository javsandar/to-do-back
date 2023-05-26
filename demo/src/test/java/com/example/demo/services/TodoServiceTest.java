package com.example.demo.services;

import com.example.demo.Controller.models.TodoFilter;
import com.example.demo.Repository.entities.TodoEntity;
import com.example.demo.Repository.repositories.TodoRepository;
import com.example.demo.Service.helpers.UUIDhelper;
import com.example.demo.Service.models.TodoDto;
import com.example.demo.Service.models.TodoFilterDto;
import com.example.demo.Service.services.TodoService;
import com.example.demo.Service.services.TodoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

import static org.assertj.core.api.FactoryBasedNavigableListAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

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
    }

//    @ParameterizedTest
//    @MethodSource("getTodosProvider")
//    public void givenTodosFilter_whenGetTodos_thenReturnFilteredTodos(TodoFilter todoFilter, List<TodoEntity> providedTodos) {
//        TodoEntity exampleTodo = new TodoEntity(null, null, todoFilter.getFinished(), todoFilter.getCreationDate(), todoFilter.getExpireDate());
//        Example<TodoEntity> example = Example.of(exampleTodo);
//        when(todoRepository.findAll(example)).thenReturn(providedTodos);
//        for (TodoEntity todo : providedTodos) {
//            assert (todo.isFinished() == todoFilter.getFinished());
//            assert (todo.getCreationDate() == todoFilter.getCreationDate());
//            assert (todo.getExpireDate() == todoFilter.getExpireDate());
//        }
//    }
//
//    private static Stream<Arguments> getTodosProvider() {
//        TodoFilter filterFinishedFalseAndExpireDateNull = new TodoFilter(false, null, LocalDate.parse("2023-05-24"));
//        TodoFilter filterFinishedNullAndExpireDate24 = new TodoFilter(null, null, LocalDate.parse("2023-05-24"));
//        TodoFilter filterFinishedNullAndExpireDate25 = new TodoFilter(null, null, LocalDate.parse("2023-05-25"));
//
//        List<TodoEntity> caseFinishedFalseAndExpireDateNull = Arrays.asList(
//                new TodoEntity(UUID.fromString("72836dd4-ec1a-4ff6-98f3-55bfeb2728d5"), "Tarea de prueba 1", false, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-24")),
//                new TodoEntity(UUID.fromString("39431462-9fbe-425f-96ba-e45f0bc67e71"), "Tarea de prueba 2", false, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-24")));
//        List<TodoEntity> caseFinishedTrueAndExpireDate24 = List.of(
//                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940c"), "Tarea de prueba 3", true, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-24")));
//        List<TodoEntity> caseFinishedNullAndExpireDate25 = List.of(
//                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940d"), "Tarea de prueba 4", false, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-25")));
//
//        return Stream.of(
//                Arguments.of(filterFinishedFalseAndExpireDateNull, caseFinishedFalseAndExpireDateNull),
//                Arguments.of(filterFinishedNullAndExpireDate24, caseFinishedTrueAndExpireDate24),
//                Arguments.of(filterFinishedNullAndExpireDate25, caseFinishedNullAndExpireDate25));
//    }

//
//    @Test
//    public void givenTodosList_whenGetRemainingTodos_thenListRemainingTodos() {
//        todos = todos.stream().filter(todo -> !todo.isFinished()).toList();
//
//        when(todoRepository.findByIsFinished(false)).thenReturn(todos);
//        List<TodoEntity> expectedList = todoService.getRemainingTodos(false);
//
//        assert (expectedList.containsAll(todos));
//    }
//
//    @Test
//    public void givenTodoRequestModel_whenAddTodo_thenTodoIsCreated() {
//        TodoRequest todoRequest = new TodoRequest("Tarea de prueba 1", false);
//        TodoEntity capturedTodoEntity = new TodoEntity(UUIDhelper.generateRandomUUID(), todoRequest.getText(), todoRequest.isFinished());
//        when(todoRepository.save(any(TodoEntity.class))).thenReturn(capturedTodoEntity);
//
//        todoService.addTodo(todoRequest);
//
//        Assertions.assertNotNull(capturedTodoEntity.getId());
//        Assertions.assertEquals(capturedTodoEntity.getText(), todoRequest.getText());
//        Assertions.assertEquals(capturedTodoEntity.isFinished(), todoRequest.isFinished());
//
//    }
//
//    @Test
//    public void givenIdParam_whenGetTodo_thenReturnTodo() {
//        UUID id = UUIDhelper.generateRandomUUID();
//        Optional<TodoEntity> expectedTodoEntity = Optional.of(new TodoEntity(id, "Tarea de prueba 1", false));
//
//        when(todoRepository.findById(id)).thenReturn(expectedTodoEntity);
//        Optional<TodoEntity> returnedTodo = todoService.getTodo(id);
//
//        Assertions.assertEquals(expectedTodoEntity, returnedTodo);
//
//    }
//
//    @Test
//    public void givenIdParam_whenUpdateTodo_thenUpdateTodoFields() {
//        TodoEntity prevTodo = new TodoEntity(UUIDhelper.generateRandomUUID(), "Tarea de prueba 1", false);
//        TodoUpdateRequest todoUpdateRequest = new TodoUpdateRequest("Tarea de prueba actualizada 1", true);
//        TodoEntity expectedTodo = new TodoEntity(prevTodo.getId(), todoUpdateRequest.getText(), todoUpdateRequest.isFinished());
//
//        when(todoRepository.findById(prevTodo.getId())).thenReturn(Optional.of(prevTodo));
//        when(todoRepository.save(any(TodoEntity.class))).thenReturn(expectedTodo);
//
//        Optional<TodoEntity> updatedTodo = Optional.of(todoService.updateTodo(prevTodo.getId(), todoUpdateRequest).get());
//
//        Assertions.assertEquals(updatedTodo.get().getId(), expectedTodo.getId());
//        Assertions.assertEquals(updatedTodo.get().getText(), expectedTodo.getText());
//        Assertions.assertEquals(updatedTodo.get().isFinished(), expectedTodo.isFinished());
//    }

}
