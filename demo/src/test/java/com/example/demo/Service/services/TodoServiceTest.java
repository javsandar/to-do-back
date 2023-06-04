package com.example.demo.Service.services;

import com.example.demo.Repository.entities.TodoEntity;
import com.example.demo.Repository.models.TodoEntityFilter;
import com.example.demo.Repository.repositories.TodoRepository;
import com.example.demo.Service.helpers.uuidHelper;
import com.example.demo.Service.models.TodoCreationDto;
import com.example.demo.Service.models.TodoDto;
import com.example.demo.Service.models.TodoFilterDto;
import com.example.demo.Service.models.TodoUpdateDto;
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
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TodoServiceTest {

    @InjectMocks
    private TodoService todoService = new TodoServiceImpl();
    @Mock
    private TodoRepository todoRepository;
    @Autowired
    private ModelMapper modelMapper = new ModelMapper();
    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @ParameterizedTest
    @MethodSource("todosProvider")
    public void givenTodoFilterDto_whenGetTodosByFilter_thenReturnFilteredTodos(TodoFilterDto todoFilterDto, List<TodoEntity> providedTodos) {
        List<TodoDto> result;
        List<TodoDto> expected;

        TodoEntityFilter todoEntityFilter = new TodoEntityFilter(todoFilterDto.getFinished(), todoFilterDto.getCreationDate(), todoFilterDto.getExpireDate());
        when(todoRepository.findTodosByFilter(any(TodoEntityFilter.class))).thenReturn(providedTodos);
        result = todoService.getTodosByFilter(todoFilterDto);
        expected = providedTodos.stream().map(todo -> modelMapper.map(todo, TodoDto.class)).collect(Collectors.toList());

        assert (result.size() == expected.size());
    }

    private static Stream<Arguments> todosProvider() {
        TodoFilterDto filterAllNull = new TodoFilterDto(null, null, null);
        TodoFilterDto filterFinishedFalseAndExpireDateNull = new TodoFilterDto(false, null, null);
        TodoFilterDto filterFinishedNullAndExpireDate24 = new TodoFilterDto(null, null, Collections.singletonList("2023-05-24"));
        TodoFilterDto filterFinishedNullAndExpireDate25 = new TodoFilterDto(null, null, Collections.singletonList("2023-05-25"));
        TodoFilterDto filterFinishedNullAndGteExpireDate24 = new TodoFilterDto(null, null, Collections.singletonList("gte:2023-05-24"));
        TodoFilterDto filterFinishedNullAndGtExpireDate24 = new TodoFilterDto(null, null, Collections.singletonList("gt:2023-05-24"));
        TodoFilterDto filterFinishedNullAndLteExpireDate24 = new TodoFilterDto(null, null, Collections.singletonList("lte:2023-05-24"));
        TodoFilterDto filterFinishedNullAndLtExpireDate24 = new TodoFilterDto(null, null, Collections.singletonList("lt:2023-05-24"));
        TodoFilterDto filterFinishedNullAndGteExpireDate24AndLteExpireDate26 = new TodoFilterDto(null, null, Arrays.asList("gte:2023-05-24", "lte:2023-05-26"));
        TodoFilterDto filterFinishedNullAndGteExpireDate24AndLtExpireDate26 = new TodoFilterDto(null, null, Arrays.asList("gte:2023-05-24", "lt:2023-05-26"));
        TodoFilterDto filterFinishedNullAndGtExpireDate24AndLteExpireDate26 = new TodoFilterDto(null, null, Arrays.asList("gt:2023-05-24", "lte:2023-05-26"));
        TodoFilterDto filterFinishedNullAndGtExpireDate24AndLtExpireDate26 = new TodoFilterDto(null, null, Arrays.asList("gt:2023-05-24", "lt:2023-05-26"));


        List<TodoEntity> caseAllNull = List.of(
                new TodoEntity(UUID.fromString("72836dd4-ec1a-4ff6-98f3-55bfeb2728d3"), "Tarea de prueba 0", false, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-23")),
                new TodoEntity(UUID.fromString("72836dd4-ec1a-4ff6-98f3-55bfeb2728d5"), "Tarea de prueba 1", false, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-24")),
                new TodoEntity(UUID.fromString("39431462-9fbe-425f-96ba-e45f0bc67e71"), "Tarea de prueba 2", false, LocalDate.parse("2023-05-25"), LocalDate.parse("2023-05-24")),
                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940c"), "Tarea de prueba 3", true, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-24")),
                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940e"), "Tarea de prueba 5", false, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-24")),
                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940d"), "Tarea de prueba 4", false, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-25")),
                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940f"), "Tarea de prueba 6", true, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-25")),
                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4941c"), "Tarea de prueba 7", true, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-26"))
        );
        List<TodoEntity> caseFinishedFalseAndExpireDateNull = Arrays.asList(
                new TodoEntity(UUID.fromString("72836dd4-ec1a-4ff6-98f3-55bfeb2728d3"), "Tarea de prueba 0", false, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-23")),
                new TodoEntity(UUID.fromString("72836dd4-ec1a-4ff6-98f3-55bfeb2728d5"), "Tarea de prueba 1", false, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-24")),
                new TodoEntity(UUID.fromString("39431462-9fbe-425f-96ba-e45f0bc67e71"), "Tarea de prueba 2", false, LocalDate.parse("2023-05-25"), LocalDate.parse("2023-05-24")),
                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940e"), "Tarea de prueba 5", false, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-24")),
                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940d"), "Tarea de prueba 4", false, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-25"))
        );
        List<TodoEntity> caseFinishedNullAndExpireDate24 = List.of(
                new TodoEntity(UUID.fromString("72836dd4-ec1a-4ff6-98f3-55bfeb2728d5"), "Tarea de prueba 1", false, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-24")),
                new TodoEntity(UUID.fromString("39431462-9fbe-425f-96ba-e45f0bc67e71"), "Tarea de prueba 2", false, LocalDate.parse("2023-05-25"), LocalDate.parse("2023-05-24")),
                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940c"), "Tarea de prueba 3", true, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-24")),
                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940e"), "Tarea de prueba 5", false, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-24"))
        );
        List<TodoEntity> caseFinishedNullAndExpireDate25 = List.of(
                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940d"), "Tarea de prueba 4", false, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-25")),
                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940f"), "Tarea de prueba 6", true, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-25")));
        List<TodoEntity> caseFinishedNullAndGteExpireDate24 = List.of(
                new TodoEntity(UUID.fromString("72836dd4-ec1a-4ff6-98f3-55bfeb2728d5"), "Tarea de prueba 1", false, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-24")),
                new TodoEntity(UUID.fromString("39431462-9fbe-425f-96ba-e45f0bc67e71"), "Tarea de prueba 2", false, LocalDate.parse("2023-05-25"), LocalDate.parse("2023-05-24")),
                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940c"), "Tarea de prueba 3", true, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-24")),
                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940e"), "Tarea de prueba 5", false, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-24")),
                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940d"), "Tarea de prueba 4", false, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-25")),
                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940f"), "Tarea de prueba 6", true, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-25")),
                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4941c"), "Tarea de prueba 7", true, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-26"))
        );
        List<TodoEntity> caseFinishedNullAndGtExpireDate24 = List.of(
                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940d"), "Tarea de prueba 4", false, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-25")),
                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940f"), "Tarea de prueba 6", true, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-25")),
                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4941c"), "Tarea de prueba 7", true, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-26"))
        );
        List<TodoEntity> caseFinishedNullAndLteExpireDate24 = List.of(
                new TodoEntity(UUID.fromString("72836dd4-ec1a-4ff6-98f3-55bfeb2728d3"), "Tarea de prueba 0", false, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-23")),
                new TodoEntity(UUID.fromString("72836dd4-ec1a-4ff6-98f3-55bfeb2728d5"), "Tarea de prueba 1", false, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-24")),
                new TodoEntity(UUID.fromString("39431462-9fbe-425f-96ba-e45f0bc67e71"), "Tarea de prueba 2", false, LocalDate.parse("2023-05-25"), LocalDate.parse("2023-05-24")),
                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940c"), "Tarea de prueba 3", true, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-24")),
                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940e"), "Tarea de prueba 5", false, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-24"))
        );
        List<TodoEntity> caseFinishedNullAndLtExpireDate24 = List.of(
                new TodoEntity(UUID.fromString("72836dd4-ec1a-4ff6-98f3-55bfeb2728d3"), "Tarea de prueba 0", false, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-23"))
        );
        List<TodoEntity> caseFinishedNullAndGteExpireDate24AndLteExpireDate26 = List.of(
                new TodoEntity(UUID.fromString("72836dd4-ec1a-4ff6-98f3-55bfeb2728d5"), "Tarea de prueba 1", false, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-24")),
                new TodoEntity(UUID.fromString("39431462-9fbe-425f-96ba-e45f0bc67e71"), "Tarea de prueba 2", false, LocalDate.parse("2023-05-25"), LocalDate.parse("2023-05-24")),
                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940c"), "Tarea de prueba 3", true, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-24")),
                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940e"), "Tarea de prueba 5", false, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-24")),
                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940d"), "Tarea de prueba 4", false, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-25")),
                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940f"), "Tarea de prueba 6", true, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-25")),
                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4941c"), "Tarea de prueba 7", true, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-26"))
        );
        List<TodoEntity> caseFinishedNullAndGteExpireDate24AndLtExpireDate26 = List.of(
                new TodoEntity(UUID.fromString("72836dd4-ec1a-4ff6-98f3-55bfeb2728d5"), "Tarea de prueba 1", false, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-24")),
                new TodoEntity(UUID.fromString("39431462-9fbe-425f-96ba-e45f0bc67e71"), "Tarea de prueba 2", false, LocalDate.parse("2023-05-25"), LocalDate.parse("2023-05-24")),
                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940c"), "Tarea de prueba 3", true, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-24")),
                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940e"), "Tarea de prueba 5", false, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-24")),
                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940d"), "Tarea de prueba 4", false, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-25")),
                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940f"), "Tarea de prueba 6", true, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-25"))
        );
        List<TodoEntity> caseFinishedNullAndGtExpireDate24AndLteExpireDate26 = List.of(
                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940d"), "Tarea de prueba 4", false, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-25")),
                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940f"), "Tarea de prueba 6", true, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-25")),
                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4941c"), "Tarea de prueba 7", true, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-26"))
        );
        List<TodoEntity> caseFinishedNullAndGtExpireDate24AndLtExpireDate26 = List.of(
                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940d"), "Tarea de prueba 4", false, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-25")),
                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940f"), "Tarea de prueba 6", true, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-25"))
        );

        return Stream.of(
                Arguments.of(filterAllNull, caseAllNull),
                Arguments.of(filterFinishedFalseAndExpireDateNull, caseFinishedFalseAndExpireDateNull),
                Arguments.of(filterFinishedNullAndExpireDate24, caseFinishedNullAndExpireDate24),
                Arguments.of(filterFinishedNullAndExpireDate25, caseFinishedNullAndExpireDate25),
                Arguments.of(filterFinishedNullAndGteExpireDate24, caseFinishedNullAndGteExpireDate24),
                Arguments.of(filterFinishedNullAndGtExpireDate24, caseFinishedNullAndGtExpireDate24),
                Arguments.of(filterFinishedNullAndLteExpireDate24, caseFinishedNullAndLteExpireDate24),
                Arguments.of(filterFinishedNullAndLtExpireDate24, caseFinishedNullAndLtExpireDate24),
                Arguments.of(filterFinishedNullAndGteExpireDate24AndLteExpireDate26, caseFinishedNullAndGteExpireDate24AndLteExpireDate26),
                Arguments.of(filterFinishedNullAndGteExpireDate24AndLtExpireDate26, caseFinishedNullAndGteExpireDate24AndLtExpireDate26),
                Arguments.of(filterFinishedNullAndGtExpireDate24AndLteExpireDate26, caseFinishedNullAndGtExpireDate24AndLteExpireDate26),
                Arguments.of(filterFinishedNullAndGtExpireDate24AndLtExpireDate26, caseFinishedNullAndGtExpireDate24AndLtExpireDate26)

        );
    }


    @Test
    public void givenTodoCreationDto_whenAddTodo_thenTodoIsCreated() {
        TodoCreationDto todoCreationDto = new TodoCreationDto("Tarea de prueba 1", false, LocalDate.of(2023, 5, 26));
        TodoEntity todoEntity = new TodoEntity(uuidHelper.generateRandomUUID(), todoCreationDto.getText(), todoCreationDto.isFinished(), LocalDate.now(), todoCreationDto.getExpireDate());
        TodoDto todoDto = new TodoDto(todoEntity.getId(), todoEntity.getText(), todoEntity.isFinished(), todoEntity.getCreationDate(), todoEntity.getExpireDate());
        when(todoRepository.save((any(TodoEntity.class)))).thenReturn(todoEntity);

        TodoDto result = todoService.addTodo(todoCreationDto);
        assert (result.getId() == todoEntity.getId());
        assert (result.getText().equals(todoEntity.getText()));
        assert (result.isFinished() == todoEntity.isFinished());
        assert (result.getCreationDate() == todoEntity.getCreationDate());
        assert (result.getExpireDate() == todoEntity.getExpireDate());

    }

    //
    @Test
    public void givenIdParam_whenGetTodo_thenReturnTodo() {
        UUID id = uuidHelper.generateRandomUUID();
        TodoEntity todoEntity = new TodoEntity(id, "Tarea de prueba 1", false, LocalDate.of(2023, 5, 24), LocalDate.of(2023, 5, 25));
        when(todoRepository.findById(any(UUID.class))).thenReturn(Optional.of(todoEntity));

        TodoDto result = todoService.getTodo(id);
        assert (result.getId() == todoEntity.getId());
        assert (result.getText().equals(todoEntity.getText()));
        assert (result.isFinished() == todoEntity.isFinished());
        assert (result.getCreationDate() == todoEntity.getCreationDate());
        assert (result.getExpireDate() == todoEntity.getExpireDate());

    }

    @Test
    public void givenIdParam_whenUpdateTodo_thenUpdateTodoFields() {
        UUID id = uuidHelper.generateRandomUUID();
        TodoUpdateDto todoUpdateDto = new TodoUpdateDto("Tarea actualizada", true, LocalDate.of(2023, 5, 26));
        when(todoRepository.findById(any(UUID.class))).thenReturn(Optional.of(new TodoEntity(id, "Tarea de prueba 1", false, LocalDate.of(2023, 5, 24), LocalDate.of(2023, 5, 25))));
        Optional<TodoEntity> prevTodo = todoRepository.findById(id);
        TodoEntity updatedTodo = new TodoEntity(prevTodo.get().getId(), todoUpdateDto.getText(), todoUpdateDto.isFinished(), prevTodo.get().getCreationDate(), todoUpdateDto.getExpireDate());
        when(todoRepository.save(any(TodoEntity.class))).thenReturn(updatedTodo);

        TodoDto result = todoService.updateTodo(id, todoUpdateDto);
        assert (result.getId() == updatedTodo.getId());
        assert (result.getText().equals(updatedTodo.getText()));
        assert (result.isFinished() == updatedTodo.isFinished());
        assert (result.getCreationDate() == updatedTodo.getCreationDate());
        assert (result.getExpireDate() == updatedTodo.getExpireDate());
    }

    /* TESTING CASOS DE ERROR */
    @Test
    public void givenInvalidIdParam_whenGetTodo_thenReturnNull() {
        UUID invalidId = uuidHelper.generateRandomUUID();
        TodoDto result = todoService.getTodo(invalidId);
        assert (result == null);
    }

    @Test
    public void givenInvalidIdParam_whenUpdateTodo_thenReturnNull() {
        UUID invalidId = uuidHelper.generateRandomUUID();
        TodoUpdateDto todoUpdateDto = new TodoUpdateDto("Tarea actualizada", true, LocalDate.of(2023, 5, 26));
        TodoDto result = todoService.updateTodo(invalidId, todoUpdateDto);
        assert (result == null);
    }

}
