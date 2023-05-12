package com.example.demo.controllers;

import com.example.demo.Controller.models.TodoRequestModel;
import com.example.demo.Repository.entities.TodoEntity;
import com.example.demo.Repository.repositories.TodoRepository;
import com.example.demo.Service.helpers.UUIDhelper;
import com.example.demo.Service.service.TodoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TodoControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @MockBean
    private TodoService todoService;
    @MockBean
    private TodoRepository todoRepository;
    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @ParameterizedTest
    @MethodSource("getTodosProvider")
    public void _whenGetTodos_thenListTodos(Boolean isFinished, List<TodoEntity> todosProvided) throws Exception {
        when(todoService.todosHandler(isFinished)).thenReturn(todosProvided);
        todosProvided.forEach(System.out::println);
        RequestBuilder request = MockMvcRequestBuilders
                .get("/todos")
                .queryParam("finished", isFinished != null ? isFinished.toString() : "")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(todosProvided)))
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
        String todoRequestModelBody = objectMapper.writeValueAsString(newTodoRequestModel);
        TodoEntity newTodoEntity = new TodoEntity(UUIDhelper.generateRandomUUID(), newTodoRequestModel.getText(), newTodoRequestModel.isFinished());
        when(todoService.addTodo(newTodoRequestModel)).thenReturn(newTodoEntity);
        String expectedBody = objectMapper.writeValueAsString(todoRequestModelBody);
        RequestBuilder request = MockMvcRequestBuilders
                .post("/todos")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(todoRequestModelBody)
                .characterEncoding(StandardCharsets.UTF_8);
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expectedBody))
                .andReturn();
    }
//
//    @Test
//    public void givenIdParam_whenGetTodo_thenListTodo() throws Exception {
//        TodoEntity todo = new TodoEntity(UUIDhelper.generateRandomUUID(), "Tarea de prueba 1", false);
//        String body = objectMapper.writeValueAsString(todo);
//        when(todoServiceImpl.getTodo(todo.getId())).thenReturn(todo);
//        RequestBuilder request = MockMvcRequestBuilders
//                .get("/todos/" + todo.getId())
//                .contentType(MediaType.APPLICATION_JSON);
//        mockMvc.perform(request)
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().json(body))
//                .andReturn();
//
//    }
//
//    @Test
//    public void givenIdParam_whenUpdateTodo_thenModifyIsFinished() throws Exception {
//        UUID id = UUIDhelper.generateRandomUUID();
//        TodoEntity todo = new TodoEntity(id, "Tarea de prueba 1", false);
//        String body = objectMapper.writeValueAsString(todo);
//        TodoEntity updatedTodo = new TodoEntity(todo.getId(), todo.getText(), !todo.isFinished());
//        String updatedBody = objectMapper.writeValueAsString(updatedTodo);
//
//        when(todoServiceImpl.updateTodo(id)).thenReturn(updatedTodo);
//        RequestBuilder request = MockMvcRequestBuilders
//                .put("/todos/" + todo.getId())
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(body);
//        mockMvc.perform(request)
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().json(updatedBody))
//                .andReturn();
//
//    }
//
//    /*
//    TESTING CASOS DE ERROR
//     */
//
//    @Test
//    public void givenWrongFinishedValue_whenAddTodo_thenThrowMethodArgumentNotValidException() throws Exception {
//        TodoRequestModel wrongTodoRequestModel = new TodoRequestModel("Tarea de prueba 1", true);
//        TodoEntity wrongTodo = new TodoEntity(UUIDhelper.generateRandomUUID(), "Tarea de prueba 1", true);
//        String body = objectMapper.writeValueAsString(wrongTodo);
//        final String ERROR_MESSAGE = "{\n" +
//                "  \"errors\": [\n" +
//                "    {\n" +
//                "      \"error\": \"AssertFalse\",\n" +
//                "      \"message\": \"Todo can not be finished\",\n" +
//                "      \"detail\": \"Error located in 'isFinished' field\"\n" +
//                "    }\n" +
//                "  ]\n" +
//                "}";
//
//        when(todoServiceImpl.addTodo(wrongTodoRequestModel)).thenReturn(wrongTodo);
//        RequestBuilder request = MockMvcRequestBuilders
//                .post("/todos")
//                .accept(MediaType.APPLICATION_JSON)
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .characterEncoding(StandardCharsets.UTF_8.name())
//                .content(body);
//        mockMvc.perform(request)
//                .andDo(print())
//                .andExpect(status().isBadRequest())
//                .andExpect(content().json(ERROR_MESSAGE))
//                .andReturn();
//
//    }
//
//    @Test
//    public void givenWrongTextValue_whenAddTodo_thenThrowMethodArgumentNotValidException() throws Exception {
//        TodoRequestModel wrongTodoRequestModel = new TodoRequestModel("", false);
//        String body = objectMapper.writeValueAsString(wrongTodoRequestModel);
//        TodoEntity wrongTodo = new TodoEntity(UUIDhelper.generateRandomUUID(), wrongTodoRequestModel.getText(), wrongTodoRequestModel.isFinished());
//        final String ERROR_MESSAGE = "{\n" +
//                "  \"errors\": [\n" +
//                "    {\n" +
//                "      \"error\": \"NotBlank\",\n" +
//                "      \"message\": \"Todo description can not be empty\",\n" +
//                "      \"detail\": \"Error located in 'text' field\"\n" +
//                "    }\n" +
//                "  ]\n" +
//                "}";
//
//        when(todoServiceImpl.addTodo(wrongTodoRequestModel)).thenReturn(wrongTodo);
//        RequestBuilder request = MockMvcRequestBuilders
//                .post("/todos")
//                .accept(MediaType.APPLICATION_JSON)
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .characterEncoding(StandardCharsets.UTF_8.name())
//                .content(body);
//        mockMvc.perform(request)
//                .andDo(print())
//                .andExpect(status().isBadRequest())
//                .andExpect(content().json(ERROR_MESSAGE))
//                .andReturn();
//
//    }
//
//    @Test
//    public void givenWrongTextAndFinishedValues_whenAddTodo_thenThrowMethodArgumentNotValidException() throws Exception {
//        TodoRequestModel wrongTodoRequestModel = new TodoRequestModel("", true);
//        Todo wrongTodo = new Todo(UUIDhelper.generateRandomUUID(), wrongTodoRequestModel.getText(), wrongTodoRequestModel.isFinished());
//        String body = objectMapper.writeValueAsString(wrongTodo);
//        final String ERROR_MESSAGE = "{\n" +
//                "  \"errors\": [\n" +
//                "    {\n" +
//                "      \"error\": \"AssertFalse\",\n" +
//                "      \"message\": \"Todo can not be finished\",\n" +
//                "      \"detail\": \"Error located in 'isFinished' field\"\n" +
//                "    },\n" +
//                "    {\n" +
//                "      \"error\": \"NotBlank\",\n" +
//                "      \"message\": \"Todo description can not be empty\",\n" +
//                "      \"detail\": \"Error located in 'text' field\"\n" +
//                "    }\n" +
//                "  ]\n" +
//                "}";
//
//        when(todoServiceImpl.addTodo(wrongTodoRequestModel)).thenReturn(wrongTodo);
//        RequestBuilder request = MockMvcRequestBuilders
//                .post("/todos")
//                .accept(MediaType.APPLICATION_JSON)
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .characterEncoding(StandardCharsets.UTF_8.name())
//                .content(body);
//        mockMvc.perform(request)
//                .andDo(print())
//                .andExpect(status().isBadRequest())
//                .andExpect(content().json(ERROR_MESSAGE))
//                .andReturn();
//    }
//
//    @Test
//    public void givenWrongIdParam_whenGetTodo_thenThrowNoSuchElementException() throws Exception {
//        UUID id = UUIDhelper.generateRandomUUID();
//        final String ERROR_MESSAGE = "{\n" +
//                "  \"error\": \"Todo not found\"\n" +
//                "}";
//        when(todoServiceImpl.getTodo(id)).thenThrow(NoSuchElementException.class);
//        RequestBuilder request = MockMvcRequestBuilders
//                .get("/todos/" + id)
//                .accept(MediaType.APPLICATION_JSON)
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .characterEncoding(StandardCharsets.UTF_8.name());
//        mockMvc.perform(request)
//                .andDo(print())
//                .andExpect(status().isNotFound())
//                .andExpect(content().json(ERROR_MESSAGE))
//                .andReturn();
//
//    }
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
