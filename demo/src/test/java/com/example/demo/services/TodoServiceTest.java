package com.example.demo.services;

import com.example.demo.Controller.models.TodoCreationRequest;
import com.example.demo.Controller.models.TodoFilter;
import com.example.demo.Repository.entities.TodoEntity;
import com.example.demo.Repository.repositories.TodoRepository;
import com.example.demo.Service.helpers.UUIDhelper;
import com.example.demo.Service.models.TodoCreationDto;
import com.example.demo.Service.models.TodoDto;
import com.example.demo.Service.models.TodoFilterDto;
import com.example.demo.Service.models.TodoUpdateDto;
import com.example.demo.Service.services.TodoService;
import com.example.demo.Service.services.TodoServiceImpl;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
import static org.mockito.Mockito.*;
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

    @ParameterizedTest
    @MethodSource("getTodosProvider")
    public void givenTodosFilter_whenGetTodos_thenReturnFilteredTodos(TodoFilterDto todoFilterDto, List<TodoEntity> providedTodos) {
        List<TodoEntity> result;
        if (todoFilterDto.getFinished() == null && todoFilterDto.getCreationDate() == null && todoFilterDto.getExpireDate() == null) {
            when(todoRepository.findAll()).thenReturn(providedTodos);

            result = todoRepository.findAll();
            assert (result.containsAll(providedTodos));
        }
        TodoEntity exampleTodo = new TodoEntity(null, null, todoFilterDto.getFinished(), todoFilterDto.getCreationDate(), todoFilterDto.getExpireDate());
        Example<TodoEntity> example = Example.of(exampleTodo);
        when(todoRepository.findAll(example)).thenReturn(providedTodos);

        result = todoRepository.findAll(example);
        assert (result.containsAll(providedTodos));
    }

    private static Stream<Arguments> getTodosProvider() {
        TodoFilterDto filterAllNull = new TodoFilterDto(null, null, null);
        TodoFilterDto filterFinishedFalseAndExpireDateNull = new TodoFilterDto(false, null, null);
        TodoFilterDto filterFinishedNullAndExpireDate24 = new TodoFilterDto(null, null, LocalDate.parse("2023-05-24"));
        TodoFilterDto filterFinishedNullAndExpireDate25 = new TodoFilterDto(null, null, LocalDate.parse("2023-05-25"));

        List<TodoEntity> caseAllNull = List.of(
                new TodoEntity(UUID.fromString("72836dd4-ec1a-4ff6-98f3-55bfeb2728d5"), "Tarea de prueba 1", false, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-24")),
                new TodoEntity(UUID.fromString("39431462-9fbe-425f-96ba-e45f0bc67e71"), "Tarea de prueba 2", false, LocalDate.parse("2023-05-25"), LocalDate.parse("2023-05-24")),
                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940c"), "Tarea de prueba 3", true, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-24")),
                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940e"), "Tarea de prueba 5", false, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-24")),
                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940d"), "Tarea de prueba 4", false, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-25")),
                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940f"), "Tarea de prueba 6", true, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-25")));
        List<TodoEntity> caseFinishedFalseAndExpireDateNull = Arrays.asList(
                new TodoEntity(UUID.fromString("72836dd4-ec1a-4ff6-98f3-55bfeb2728d5"), "Tarea de prueba 1", false, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-24")),
                new TodoEntity(UUID.fromString("39431462-9fbe-425f-96ba-e45f0bc67e71"), "Tarea de prueba 2", false, LocalDate.parse("2023-05-25"), LocalDate.parse("2023-05-24")));
        List<TodoEntity> caseFinishedNullAndExpireDate24 = List.of(
                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940c"), "Tarea de prueba 3", true, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-24")),
                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940e"), "Tarea de prueba 5", false, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-24")));
        List<TodoEntity> caseFinishedNullAndExpireDate25 = List.of(
                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940d"), "Tarea de prueba 4", false, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-25")),
                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940f"), "Tarea de prueba 6", true, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-25")));


        return Stream.of(
                Arguments.of(filterAllNull, caseAllNull),
                Arguments.of(filterFinishedFalseAndExpireDateNull, caseFinishedFalseAndExpireDateNull),
                Arguments.of(filterFinishedNullAndExpireDate24, caseFinishedNullAndExpireDate24),
                Arguments.of(filterFinishedNullAndExpireDate25, caseFinishedNullAndExpireDate25));
    }

    @Test
    public void givenNoFilter_whenGetTodos_thenReturnAllTodos() {
        List<TodoEntity> expectedTodos = List.of(
                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940d"), "Tarea de prueba 4", false, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-25")),
                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940f"), "Tarea de prueba 6", true, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-26")));
        when(todoRepository.findAll()).thenReturn(expectedTodos);

        List<TodoEntity> result = todoRepository.findAll();
        assert (result.containsAll(expectedTodos));
    }


    @Test
    public void givenTodoCreationDto_whenAddTodo_thenTodoIsCreated() {
        TodoCreationDto todoCreationDto = new TodoCreationDto("Tarea de prueba 1", false, LocalDate.of(2023, 5, 26));
        TodoEntity todoEntity = new TodoEntity(UUIDhelper.generateRandomUUID(), todoCreationDto.getText(), todoCreationDto.isFinished(), LocalDate.now(), todoCreationDto.getExpireDate());
        TodoDto todoDto = new TodoDto(todoEntity.getId(), todoEntity.getText(), todoEntity.isFinished(), todoEntity.getCreationDate(), todoEntity.getExpireDate());
        when(todoRepository.save(todoEntity)).thenReturn(todoEntity);

        TodoEntity result = todoRepository.save(todoEntity);
        assert (result.getId() == todoEntity.getId());
        assert (result.getText().equals(todoEntity.getText()));
        assert (result.isFinished() == todoEntity.isFinished());
        assert (result.getCreationDate() == todoEntity.getCreationDate());
        assert (result.getExpireDate() == todoEntity.getExpireDate());

    }

    //
    @Test
    public void givenIdParam_whenGetTodo_thenReturnTodo() {
        UUID id = UUIDhelper.generateRandomUUID();
        TodoEntity todoEntity = new TodoEntity(id, "Tarea de prueba 1", false, LocalDate.of(2023, 5, 24), LocalDate.of(2023, 5, 25));
        when(todoRepository.findById(id)).thenReturn(Optional.of(todoEntity));

        Optional<TodoEntity> result = todoRepository.findById(id);
        assert (result.get().getId() == todoEntity.getId());
        assert (result.get().getText().equals(todoEntity.getText()));
        assert (result.get().isFinished() == todoEntity.isFinished());
        assert (result.get().getCreationDate() == todoEntity.getCreationDate());
        assert (result.get().getExpireDate() == todoEntity.getExpireDate());

    }

    @Test
    public void givenIdParam_whenUpdateTodo_thenUpdateTodoFields() {
        UUID id = UUIDhelper.generateRandomUUID();
        TodoUpdateDto todoUpdateDto = new TodoUpdateDto("Tarea actualizada", true, LocalDate.of(2023, 5, 26));
        when(todoRepository.findById(id)).thenReturn(Optional.of(new TodoEntity(id, "Tarea de prueba 1", false, LocalDate.of(2023, 5, 24), LocalDate.of(2023, 5, 25))));
        Optional<TodoEntity> prevTodo = todoRepository.findById(id);
        TodoEntity updatedTodo = new TodoEntity(prevTodo.get().getId(), todoUpdateDto.getText(), todoUpdateDto.isFinished(), prevTodo.get().getCreationDate(), todoUpdateDto.getExpireDate());
        when(todoRepository.save(updatedTodo)).thenReturn(updatedTodo);

        TodoEntity result = todoRepository.save(updatedTodo);
        assert (result.getId() == updatedTodo.getId());
        assert (result.getText().equals(updatedTodo.getText()));
        assert (result.isFinished() == updatedTodo.isFinished());
        assert (result.getCreationDate() == updatedTodo.getCreationDate());
        assert (result.getExpireDate() == updatedTodo.getExpireDate());
    }

    /* TESTING CASOS DE ERROR */
    @Test
    public void givenInvalidIdParam_whenGetTodo_thenReturnNull() {
        UUID invalidId = UUIDhelper.generateRandomUUID();
        when(todoRepository.findById(invalidId)).thenReturn(null);
        Optional<TodoEntity> result = todoRepository.findById(invalidId);
        assert (result == null);
    }

    @Test
    public void givenInvalidIdParam_whenUpdateTodo_thenReturnNull() {
        UUID invalidId = UUIDhelper.generateRandomUUID();
        when(todoRepository.findById(invalidId)).thenReturn(null);
        Optional<TodoEntity> result = todoRepository.findById(invalidId);
        assert (result == null);
    }

}
