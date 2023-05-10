package com.example.demo.controllers;

import com.example.demo.helpers.UUIDhelper;
import com.example.demo.models.Todo;
import com.example.demo.models.TodoPutRequest;
import com.example.demo.models.TodoRequest;
import com.example.demo.repositories.TodoRepository;
import com.example.demo.services.TodoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@EnableWebMvc
public class TodoControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @MockBean
    private TodoService todoService;
    @MockBean
    private TodoRepository todoRepository;
    private List<Todo> todos;
    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        todos = new ArrayList<>(Arrays.asList(
                new Todo(UUIDhelper.generateRandomUUID(), "Tarea de prueba 0", false),
                new Todo(UUIDhelper.generateRandomUUID(), "Tarea de prueba 1", false),
                new Todo(UUIDhelper.generateRandomUUID(), "Tarea de prueba 2", true)));
    }

    //Test de endpoints
    @Test
    public void givenNoParam_whenGetTodos_thenListAllTodos() throws Exception {
        when(todoService.todosHandler(any())).thenReturn(todos);
        RequestBuilder request = MockMvcRequestBuilders
                .get("/todos")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(todos)))
                .andReturn();
    }

    @Test
    public void givenFinishedParam_whenGetTodos_thenListRemainingTodos() throws Exception {
        Boolean finished = false;
        List<Todo> remainingTodos = todos.stream().filter(todo -> !todo.isFinished()).toList();
        when(todoService.todosHandler(finished)).thenReturn(remainingTodos);
        RequestBuilder request = MockMvcRequestBuilders
                .get("/todos")
                .queryParam("finished", "false")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(remainingTodos)))
                .andReturn();
    }

    @Test
    public void givenNewTodo_whenAddTodo_thenTodoIsAddedToList() throws Exception {
        TodoRequest newTodoRequest = new TodoRequest("Tarea de prueba 1", false);
        Todo newTodo = new Todo(UUIDhelper.generateRandomUUID(), newTodoRequest.getText(), newTodoRequest.isFinished());
        String body = objectMapper.writeValueAsString(newTodo);
        when(todoService.addTodo(newTodoRequest)).thenReturn(newTodo);
        RequestBuilder request = MockMvcRequestBuilders
                .post("/todos")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding(StandardCharsets.UTF_8.name())
                .content(body);
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void givenIdParam_whenGetTodo_thenListTodo() throws Exception {
        Todo todo = new Todo(UUIDhelper.generateRandomUUID(), "Tarea de prueba 1", false);
        String body = objectMapper.writeValueAsString(todo);
        when(todoService.getTodo(todo.getId())).thenReturn(todo);
        RequestBuilder request = MockMvcRequestBuilders
                .get("/todos/" + todo.getId())
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(body))
                .andReturn();

    }

    @Test
    public void givenIdParam_whenUpdateTodo_thenModifyIsFinished() throws Exception {
        UUID id = UUIDhelper.generateRandomUUID();
        Todo todo = new Todo(id, "Tarea de prueba 1", false);
        String body = objectMapper.writeValueAsString(todo);
        Todo updatedTodo = new Todo(todo.getId(), todo.getText(), !todo.isFinished());
        String updatedBody = objectMapper.writeValueAsString(updatedTodo);

        when(todoService.updateTodo(id)).thenReturn(updatedTodo);
        RequestBuilder request = MockMvcRequestBuilders
                .put("/todos/" + todo.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(body);
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(updatedBody))
                .andReturn();

    }

    /*
    TESTING CASOS DE ERROR
     */

    @Test
    public void givenWrongFinishedValue_whenAddTodo_thenThrowMethodArgumentNotValidException() throws Exception {
        TodoRequest wrongTodoRequest = new TodoRequest("Tarea de prueba 1", true);
        Todo wrongTodo = new Todo(UUIDhelper.generateRandomUUID(), "Tarea de prueba 1", true);
        String body = objectMapper.writeValueAsString(wrongTodo);
        final String ERROR_MESSAGE = "{\n" +
                "  \"errors\": [\n" +
                "    {\n" +
                "      \"error\": \"AssertFalse\",\n" +
                "      \"message\": \"Todo can not be finished\",\n" +
                "      \"detail\": \"Error located in 'isFinished' field\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        when(todoService.addTodo(wrongTodoRequest)).thenReturn(wrongTodo);
        RequestBuilder request = MockMvcRequestBuilders
                .post("/todos")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding(StandardCharsets.UTF_8.name())
                .content(body);
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json(ERROR_MESSAGE))
                .andReturn();

    }

    @Test
    public void givenWrongTextValue_whenAddTodo_thenThrowMethodArgumentNotValidException() throws Exception {
        TodoRequest wrongTodoRequest = new TodoRequest("", false);
        String body = objectMapper.writeValueAsString(wrongTodoRequest);
        Todo wrongTodo = new Todo(UUIDhelper.generateRandomUUID(), wrongTodoRequest.getText(), wrongTodoRequest.isFinished());
        final String ERROR_MESSAGE = "{\n" +
                "  \"errors\": [\n" +
                "    {\n" +
                "      \"error\": \"NotBlank\",\n" +
                "      \"message\": \"Todo description can not be empty\",\n" +
                "      \"detail\": \"Error located in 'text' field\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        when(todoService.addTodo(wrongTodoRequest)).thenReturn(wrongTodo);
        RequestBuilder request = MockMvcRequestBuilders
                .post("/todos")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding(StandardCharsets.UTF_8.name())
                .content(body);
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json(ERROR_MESSAGE))
                .andReturn();

    }

    @Test
    public void givenWrongTextAndFinishedValues_whenAddTodo_thenThrowMethodArgumentNotValidException() throws Exception {
        TodoRequest wrongTodoRequest = new TodoRequest("", true);
        Todo wrongTodo = new Todo(UUIDhelper.generateRandomUUID(), wrongTodoRequest.getText(), wrongTodoRequest.isFinished());
        String body = objectMapper.writeValueAsString(wrongTodo);
        final String ERROR_MESSAGE = "{\n" +
                "  \"errors\": [\n" +
                "    {\n" +
                "      \"error\": \"AssertFalse\",\n" +
                "      \"message\": \"Todo can not be finished\",\n" +
                "      \"detail\": \"Error located in 'isFinished' field\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"error\": \"NotBlank\",\n" +
                "      \"message\": \"Todo description can not be empty\",\n" +
                "      \"detail\": \"Error located in 'text' field\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        when(todoService.addTodo(wrongTodoRequest)).thenReturn(wrongTodo);
        RequestBuilder request = MockMvcRequestBuilders
                .post("/todos")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding(StandardCharsets.UTF_8.name())
                .content(body);
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json(ERROR_MESSAGE))
                .andReturn();
    }

    @Test
    public void givenWrongIdParam_whenGetTodo_thenThrowNoSuchElementException() throws Exception {
        UUID id = UUIDhelper.generateRandomUUID();
        final String ERROR_MESSAGE = "{\n" +
                "  \"error\": \"Todo not found\"\n" +
                "}";
        when(todoService.getTodo(id)).thenThrow(NoSuchElementException.class);
        RequestBuilder request = MockMvcRequestBuilders
                .get("/todos/" + id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding(StandardCharsets.UTF_8.name());
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().json(ERROR_MESSAGE))
                .andReturn();

    }

    @Test
    public void givenWrongIdParam_whenUpdateTodo_thenThrowNoSuchElementException() throws Exception {
        TodoPutRequest todo = new TodoPutRequest("Tarea de prueba 1", false);
        String body = "{\"text\": " + "\"todo.getText()\"" + ",\"finished\": " + !todo.isFinished() + "}";
        final String ERROR_MESSAGE = "{\n" +
                "  \"error\": \"Todo not found\"\n" +
                "}";
        UUID id = UUIDhelper.generateRandomUUID();
        when(todoService.updateTodo(id)).thenThrow(NoSuchElementException.class);
        RequestBuilder request = MockMvcRequestBuilders
                .put("/todos/" + id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding(StandardCharsets.UTF_8.name())
                .content(body);
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().json(ERROR_MESSAGE))
                .andReturn();

    }

}
