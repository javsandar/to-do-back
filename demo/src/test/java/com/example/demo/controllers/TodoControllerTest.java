package com.example.demo.controllers;

import com.example.demo.Controller.controllers.TodoController;
import com.example.demo.Controller.models.TodoRequestModel;
import com.example.demo.Controller.models.TodoUpdateRequestModel;
import com.example.demo.Repository.entities.TodoEntity;
import com.example.demo.Service.helpers.UUIDhelper;
import com.example.demo.Service.service.TodoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = {TodoController.class})
@AutoConfigureMockMvc
@EnableWebMvc
public class TodoControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Mock
    TodoService todoService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @ParameterizedTest
    @MethodSource("getTodosProvider")
    public void _whenGetTodos_thenListTodos(Boolean isFinished, List<TodoEntity> todosProvided) throws Exception {
        given(todoService.todosHandler(isFinished)).willReturn(todosProvided);
        String expectedBody = objectMapper.writeValueAsString(todosProvided);
        RequestBuilder request = MockMvcRequestBuilders
                .get("/todos")
                .queryParam("finished", isFinished != null ? isFinished.toString() : "")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expectedBody))
                .andReturn();

    }

    private static Stream<Arguments> getTodosProvider() {
        List<TodoEntity> allTodos = Arrays.asList(
                new TodoEntity(UUID.fromString("72836dd4-ec1a-4ff6-98f3-55bfeb2728d5"), "Tarea de prueba 1", false),
                new TodoEntity(UUID.fromString("39431462-9fbe-425f-96ba-e45f0bc67e71"), "Tarea de prueba 2", false),
                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940c"), "Tarea de prueba 3", true)
        );
        List<TodoEntity> remainingTodos = Arrays.asList(
                new TodoEntity(UUID.fromString("72836dd4-ec1a-4ff6-98f3-55bfeb2728d5"), "Tarea de prueba 1", false),
                new TodoEntity(UUID.fromString("39431462-9fbe-425f-96ba-e45f0bc67e71"), "Tarea de prueba 2", false)
        );

        return Stream.of(
                Arguments.of(null, allTodos),
                Arguments.of(false, remainingTodos)
        );

    }

    @Test
    public void givenTodoRequestModel_whenAddTodo_thenTodoIsCreated() throws Exception {
        TodoRequestModel newTodoRequestModel = new TodoRequestModel("Tarea de prueba 1", false);
        TodoEntity expectedTodoEntity = new TodoEntity(UUIDhelper.generateRandomUUID(), "Tarea de prueba 1", false);

        String todoRequestModelBody = objectMapper.writeValueAsString(newTodoRequestModel);
        when(todoService.addTodo(any(TodoRequestModel.class))).thenReturn(expectedTodoEntity);
        RequestBuilder request = MockMvcRequestBuilders
                .post("/todos")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(todoRequestModelBody)
                .characterEncoding(StandardCharsets.UTF_8);
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expectedTodoEntity.getId().toString()))
                .andExpect(jsonPath("$.text").value(expectedTodoEntity.getText()))
                .andExpect(jsonPath("$.finished").value(expectedTodoEntity.isFinished()))
                .andReturn();
    }

    @Test
    public void givenIdParam_whenGetTodo_thenListTodo() throws Exception {
        TodoEntity expectedTodoEntity = new TodoEntity(UUIDhelper.generateRandomUUID(), "Tarea de prueba 1", false);
        String body = objectMapper.writeValueAsString(expectedTodoEntity);
        given(todoService.getTodo(expectedTodoEntity.getId())).willReturn(Optional.of(expectedTodoEntity));
        RequestBuilder request = MockMvcRequestBuilders
                .get("/todos/" + expectedTodoEntity.getId())
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(body))
                .andReturn();

    }

    @Test
    public void givenIdParam_whenUpdateTodo_thenModifyIsFinished() throws Exception {
        TodoEntity prevTodo = new TodoEntity(UUIDhelper.generateRandomUUID(), "Tarea de prueba 1", false);

        TodoUpdateRequestModel newTodoUpdateRequestModel = new TodoUpdateRequestModel(prevTodo.getText(), true);
        String updateBody = objectMapper.writeValueAsString(newTodoUpdateRequestModel);

        Optional<TodoEntity> expectedTodoEntity = Optional.of(new TodoEntity(prevTodo.getId(), newTodoUpdateRequestModel.getText(), newTodoUpdateRequestModel.isFinished()));

        given(todoService.updateTodo(any(UUID.class), any(TodoUpdateRequestModel.class))).willReturn(expectedTodoEntity);

        RequestBuilder request = MockMvcRequestBuilders
                .put("/todos/" + prevTodo.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateBody)
                .characterEncoding(StandardCharsets.UTF_8);
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expectedTodoEntity.get().getId().toString()))
                .andExpect(jsonPath("$.text").value(expectedTodoEntity.get().getText()))
                .andExpect(jsonPath("$.finished").value(expectedTodoEntity.get().isFinished()))
                .andReturn();

    }

//    /*
//    TESTING CASOS DE ERROR
//     */

    @ParameterizedTest
    @MethodSource("provideFields")
    public void givenWrongFinishedValue_whenAddTodo_thenThrowMethodArgumentNotValidException(String text, boolean isFinished) throws Exception {
        TodoRequestModel wrongTodoRequestModel = new TodoRequestModel(text, isFinished);
        String wrongTodoRequestModelBody = objectMapper.writeValueAsString(wrongTodoRequestModel);
        RequestBuilder request = MockMvcRequestBuilders
                .post("/todos")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding(StandardCharsets.UTF_8.name())
                .content(wrongTodoRequestModelBody);
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andReturn();
    }

    private static Stream<Arguments> provideFields() {
        /* Se comprueban las 3 opciones:
            - Texto incorrecto y finished correcto
            - Texto correcto y finished incorrecto
            - Ambos campos incorrectos
        */
        return Stream.of(
                Arguments.of("", false),
                Arguments.of("Tarea de prueba 1", true),
                Arguments.of("", true)
        );
    }

    @Test
    public void givenWrongIdParam_whenGetTodo_thenThrowNoSuchElementException() throws Exception {
        UUID invalidId = UUIDhelper.generateRandomUUID();
        when(todoService.getTodo(invalidId)).thenThrow(NoSuchElementException.class);
        RequestBuilder request = MockMvcRequestBuilders
                .get("/todos/" + invalidId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE);
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isNotFound())
//                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof NoSuchElementException))
                .andReturn();

    }
//
//    @Test
//    public void givenWrongIdParam_whenUpdateTodo_thenThrowNoSuchElementException() throws Exception {
//        TodoUpdateRequestModel todo = new TodoUpdateRequestModel("Tarea de prueba 1", false);
//        String body = "{\"text\": " + "\"todo.getText()\"" + ",\"finished\": " + !todo.isFinished() + "}";
//        final String ERROR_MESSAGE = "{\n" +
//                "  \"error\": \"Todo not found\"\n" +
//                "}";
//        UUID id = UUIDhelper.generateRandomUUID();
//        when(todoServiceImpl.updateTodo(id)).thenThrow(NoSuchElementException.class);
//        RequestBuilder request = MockMvcRequestBuilders
//                .put("/todos/" + id)
//                .accept(MediaType.APPLICATION_JSON)
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .characterEncoding(StandardCharsets.UTF_8.name())
//                .content(body);
//        mockMvc.perform(request)
//                .andDo(print())
//                .andExpect(status().isNotFound())
//                .andExpect(content().json(ERROR_MESSAGE))
//                .andReturn();
//
//    }

}
