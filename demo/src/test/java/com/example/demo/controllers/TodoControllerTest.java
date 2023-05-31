package com.example.demo.controllers;

import com.example.demo.Controller.models.TodoCreationRequest;
import com.example.demo.Controller.models.TodoFilter;
import com.example.demo.Controller.models.TodoResponse;
import com.example.demo.Controller.models.TodoUpdateRequest;
import com.example.demo.Service.helpers.uuidHelper;
import com.example.demo.Service.models.TodoCreationDto;
import com.example.demo.Service.models.TodoDto;
import com.example.demo.Service.models.TodoFilterDto;
import com.example.demo.Service.models.TodoUpdateDto;
import com.example.demo.Service.services.TodoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Assertions;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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

    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

    public TodoControllerTest() {
    }

    @BeforeEach
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @ParameterizedTest
    @MethodSource("getTodosProvider")
    public void givenTodoFilter_whenGetTodos_thenReturnFilteredTodos(TodoFilter todoFilter, List<TodoDto> providedTodos, List<TodoResponse> expectedResponse) throws Exception {
        when(todoService.getTodosByFilter(any(TodoFilterDto.class))).thenReturn(providedTodos);

        RequestBuilder request = MockMvcRequestBuilders
                .get("/todos")
                .param("finished", todoFilter.getFinished() != null ? todoFilter.getFinished().toString() : "")
                .param("creationDate", todoFilter.getCreationDate() != null ? todoFilter.getCreationDate().toString() : "")
                .param("expireDate", todoFilter.getExpireDate() != null ? todoFilter.getExpireDate().toString() : "")
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)))
                .andReturn();

    }

    private static Stream<Arguments> getTodosProvider() {
        TodoFilter filterAllNull = new TodoFilter(null, null, null);
        TodoFilter filterFinishedFalseAndExpireDateNull = new TodoFilter(false, null, Collections.singletonList("2023-05-24"));
        TodoFilter filterFinishedNullAndExpireDate24 = new TodoFilter(null, null, Collections.singletonList("2023-05-24"));
        TodoFilter filterFinishedNullAndExpireDate25 = new TodoFilter(null, null, Collections.singletonList("2023-05-25"));

        List<TodoDto> caseAllNull = Arrays.asList(
                new TodoDto(UUID.fromString("72836dd4-ec1a-4ff6-98f3-55bfeb2728d5"), "Tarea de prueba 1", false, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-24")),
                new TodoDto(UUID.fromString("39431462-9fbe-425f-96ba-e45f0bc67e71"), "Tarea de prueba 2", false, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-24")),
                new TodoDto(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940c"), "Tarea de prueba 3", true, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-24")),
                new TodoDto(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940d"), "Tarea de prueba 4", false, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-25")));
        List<TodoDto> caseFinishedFalseAndExpireDateNull = Arrays.asList(
                new TodoDto(UUID.fromString("72836dd4-ec1a-4ff6-98f3-55bfeb2728d5"), "Tarea de prueba 1", false, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-24")),
                new TodoDto(UUID.fromString("39431462-9fbe-425f-96ba-e45f0bc67e71"), "Tarea de prueba 2", false, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-24")));
        List<TodoDto> caseFinishedTrueAndExpireDate24 = List.of(
                new TodoDto(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940c"), "Tarea de prueba 3", true, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-24")));
        List<TodoDto> caseFinishedNullAndExpireDate25 = List.of(
                new TodoDto(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940d"), "Tarea de prueba 4", false, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-25")));

        List<TodoResponse> caseAllNullResponse = Arrays.asList(
                new TodoResponse(UUID.fromString("72836dd4-ec1a-4ff6-98f3-55bfeb2728d5"), "Tarea de prueba 1", false, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-24")),
                new TodoResponse(UUID.fromString("39431462-9fbe-425f-96ba-e45f0bc67e71"), "Tarea de prueba 2", false, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-24")),
                new TodoResponse(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940c"), "Tarea de prueba 3", true, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-24")),
                new TodoResponse(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940d"), "Tarea de prueba 4", false, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-25")));
        List<TodoResponse> caseFinishedFalseAndExpireDateNullResponse = Arrays.asList(
                new TodoResponse(UUID.fromString("72836dd4-ec1a-4ff6-98f3-55bfeb2728d5"), "Tarea de prueba 1", false, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-24")),
                new TodoResponse(UUID.fromString("39431462-9fbe-425f-96ba-e45f0bc67e71"), "Tarea de prueba 2", false, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-24")));
        List<TodoResponse> caseFinishedTrueAndExpireDate24Response = List.of(
                new TodoResponse(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940c"), "Tarea de prueba 3", true, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-24")));
        List<TodoResponse> caseFinishedNullAndExpireDate25Response = List.of(
                new TodoResponse(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940d"), "Tarea de prueba 4", false, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-25")));

        return Stream.of(
                Arguments.of(filterAllNull, caseAllNull, caseAllNullResponse),
                Arguments.of(filterFinishedFalseAndExpireDateNull, caseFinishedFalseAndExpireDateNull, caseFinishedFalseAndExpireDateNullResponse),
                Arguments.of(filterFinishedNullAndExpireDate24, caseFinishedTrueAndExpireDate24, caseFinishedTrueAndExpireDate24Response),
                Arguments.of(filterFinishedNullAndExpireDate25, caseFinishedNullAndExpireDate25, caseFinishedNullAndExpireDate25Response));

    }

    //
    @Test
    public void givenTodoCreationRequest_whenAddTodo_thenTodoIsCreated() throws Exception {
        TodoCreationRequest todoCreationRequest = new TodoCreationRequest("Tarea 1", false, LocalDate.of(2023, 5, 25));
        String requestBody = objectMapper.writeValueAsString(todoCreationRequest);
        TodoDto todoDto = new TodoDto(uuidHelper.generateRandomUUID(), todoCreationRequest.getText(), todoCreationRequest.isFinished(), LocalDate.of(2023, 5, 24), LocalDate.of(2023, 5, 25));
        TodoResponse expectedTodoResponse = new TodoResponse(todoDto.getId(), todoDto.getText(), todoDto.isFinished(), todoDto.getCreationDate(), todoDto.getExpireDate());
        String expectedTodoResponseBody = objectMapper.writeValueAsString(expectedTodoResponse);
        when(todoService.addTodo(any(TodoCreationDto.class))).thenReturn(todoDto);

        RequestBuilder request = MockMvcRequestBuilders
                .post("/todos")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody)
                .characterEncoding(StandardCharsets.UTF_8);
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(expectedTodoResponseBody))
                .andReturn();
    }

    @Test
    public void givenIdParam_whenGetTodo_thenListTodo() throws Exception {
        UUID id = uuidHelper.generateRandomUUID();
        TodoDto todoDto = new TodoDto(id, "Tarea 1", false, LocalDate.of(2023, 5, 24), LocalDate.of(2023, 5, 25));
        when(todoService.getTodo(id)).thenReturn(todoDto);
        TodoResponse expectedTodoResponse = new TodoResponse(todoDto.getId(), todoDto.getText(), todoDto.isFinished(), todoDto.getCreationDate(), todoDto.getExpireDate());
        String expectedTodoResponseBody = objectMapper.writeValueAsString(expectedTodoResponse);

        RequestBuilder request = MockMvcRequestBuilders
                .get("/todos/" + id)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expectedTodoResponseBody))
                .andReturn();
    }

    //
    @Test
    public void givenIdParam_whenUpdateTodo_thenModifyIsFinished() throws Exception {
        UUID id = uuidHelper.generateRandomUUID();
        TodoUpdateRequest todoUpdateRequest = new TodoUpdateRequest("Tarea 1", true, LocalDate.of(2023, 5, 25));
        String requestBody = objectMapper.writeValueAsString(todoUpdateRequest);
        TodoDto todoDto = new TodoDto(id, todoUpdateRequest.getText(), todoUpdateRequest.isFinished(), LocalDate.of(2023, 5, 24), todoUpdateRequest.getExpireDate());
        TodoResponse expectedResponse = new TodoResponse(todoDto.getId(), todoDto.getText(), todoDto.isFinished(), todoDto.getCreationDate(), todoDto.getExpireDate());
        String expectedResponseBody = objectMapper.writeValueAsString(expectedResponse);
        when(todoService.updateTodo(eq(id), any(TodoUpdateDto.class))).thenReturn(todoDto);

        RequestBuilder request = MockMvcRequestBuilders
                .put("/todos/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .characterEncoding(StandardCharsets.UTF_8);
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponseBody))
                .andReturn();

    }

    /*
    TESTING CASOS DE ERROR
     */

    @ParameterizedTest
    @MethodSource("provideFieldsForAdd")
    public void givenWrongValues_whenAddTodo_thenThrowMethodArgumentNotValidException(String text, boolean isFinished) throws Exception {
        TodoCreationRequest wrongTodoCreationRequest = new TodoCreationRequest(text, isFinished, LocalDate.of(2023, 5, 25));
        String wrongRequestBody = objectMapper.writeValueAsString(wrongTodoCreationRequest);

        RequestBuilder request = MockMvcRequestBuilders
                .post("/todos")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding(StandardCharsets.UTF_8.name())
                .content(wrongRequestBody);
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andReturn();
    }

    private static Stream<Arguments> provideFieldsForAdd() {
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
    public void givenWrongIdParam_whenGetTodo_thenThrowResponseStatusException() throws Exception {
        UUID invalidId = uuidHelper.generateRandomUUID();
        when(todoService.getTodo(invalidId)).thenReturn(null);

        RequestBuilder request = MockMvcRequestBuilders
                .get("/todos/" + invalidId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE);
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof ResponseStatusException))
                .andReturn();
    }

    @Test
    public void givenWrongIdParam_whenUpdateTodo_thenThrowResponseStatusException() throws Exception {
        UUID invalidId = uuidHelper.generateRandomUUID();
        TodoUpdateRequest todoUpdateRequest = new TodoUpdateRequest("Tarea 1", true, LocalDate.of(2023, 5, 25));
        String requestBody = objectMapper.writeValueAsString(todoUpdateRequest);
        when(todoService.updateTodo(eq(invalidId), any(TodoUpdateDto.class))).thenReturn(null);

        RequestBuilder request = MockMvcRequestBuilders
                .put("/todos/" + invalidId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding(StandardCharsets.UTF_8.name())
                .content(requestBody);
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof ResponseStatusException))
                .andReturn();
    }

    @ParameterizedTest
    @MethodSource("provideFieldsForUpdate")
    public void givenWrongValues_whenUpdate_thenThrowMethodArgumentNotValidException(String text) throws Exception {
        UUID id = uuidHelper.generateRandomUUID();
        TodoUpdateRequest wrongTodoUpdateRequest = new TodoUpdateRequest(text, true, LocalDate.of(2023, 5, 25));
        String wrongRequestBody = objectMapper.writeValueAsString(wrongTodoUpdateRequest);

        RequestBuilder request = MockMvcRequestBuilders
                .put("/todos/"+ id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding(StandardCharsets.UTF_8.name())
                .content(wrongRequestBody);
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andReturn();
    }

    private static Stream<Arguments> provideFieldsForUpdate() {
        /* Se comprueban las 2 opciones:
            - Texto vacio
            - Texto mayor de 20 caracteres
        */
        return Stream.of(
                Arguments.of(""),
                Arguments.of("Tarea de prueba de error con más de 20 caracteres")
        );
    }

}
