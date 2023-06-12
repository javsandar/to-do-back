package com.example.demo.controllers;

import com.example.demo.controllers.models.TodoCreationRequest;
import com.example.demo.controllers.models.TodoFilterRequest;
import com.example.demo.controllers.models.TodoUpdateRequest;
import com.example.demo.services.TodoService;
import com.example.demo.services.helpers.uuidHelper;
import com.example.demo.services.models.Todo;
import com.example.demo.services.models.TodoCreation;
import com.example.demo.services.models.TodoFilter;
import com.example.demo.services.models.TodoUpdate;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
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
import static org.mockito.Mockito.verify;
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
    private final ModelMapper modelMapper = new ModelMapper();

    public TodoControllerTest() {
    }

    @BeforeEach
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @ParameterizedTest
    @MethodSource("todosProvider")
    @DisplayName("GetTodos")
    public void givenTodoFilter_whenGetTodos_thenReturnFilteredTodos(TodoFilterRequest todoFilterRequest, List<Todo> expectedResponse) throws Exception {
        //given
        //when
        when(todoService.getTodosByFilter(any(TodoFilter.class))).thenReturn(expectedResponse);
        String expectedResponseBody = objectMapper.writeValueAsString(expectedResponse);
        RequestBuilder request;
        request = MockMvcRequestBuilders.get("/todos").queryParam("finished", todoFilterRequest.getFinished() != null ? todoFilterRequest.getFinished().toString() : "").queryParam("creationDate", todoFilterRequest.getCreationDate() != null ? todoFilterRequest.getCreationDate().toString() : "").queryParam("expireDate", todoFilterRequest.getExpireDate() != null ? todoFilterRequest.getExpireDate().get(0) : "").contentType(MediaType.APPLICATION_JSON);
        //then
        MvcResult mvcResult = mockMvc.perform(request).andDo(print()).andExpect(status().isOk()).andExpect(content().string(expectedResponseBody)).andReturn();
        verify(todoService, Mockito.times(1)).getTodosByFilter(any(TodoFilter.class));
    }

    private static Stream<Arguments> todosProvider() {
        TodoFilterRequest filterAllNull = new TodoFilterRequest(null, null, null);
        TodoFilterRequest filterFinishedFalse = new TodoFilterRequest(false, null, null);
        TodoFilterRequest filterExpireDate = new TodoFilterRequest(null, null, Collections.singletonList("2023-06-01"));
        TodoFilterRequest filterFinishedAndExpireDate = new TodoFilterRequest(true, null, Collections.singletonList("2023-06-06"));

        TodoFilterRequest filterGteExpireDate = new TodoFilterRequest(null, null, Collections.singletonList("gte:2023-06-01"));
        TodoFilterRequest filterGtExpireDate = new TodoFilterRequest(null, null, Collections.singletonList("gt:2023-06-01"));
        TodoFilterRequest filterLteExpireDate = new TodoFilterRequest(null, null, Collections.singletonList("lte:2023-06-10"));
        TodoFilterRequest filterLtExpireDate = new TodoFilterRequest(null, null, Collections.singletonList("lt:2023-06-10"));

        TodoFilterRequest filterGteLteExpireDate = new TodoFilterRequest(null, null, Arrays.asList("gte:2023-06-01", "lte:2023-06-10"));
        TodoFilterRequest filterGteLtExpireDate = new TodoFilterRequest(null, null, Arrays.asList("gte:2023-06-01", "lt:2023-06-10"));
        TodoFilterRequest filterGtLteExpireDate = new TodoFilterRequest(null, null, Arrays.asList("gt:2023-06-01", "lte:2023-06-10"));
        TodoFilterRequest filterGtLtExpireDate = new TodoFilterRequest(null, null, Arrays.asList("gt:2023-06-01", "lt:2023-06-10"));

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
    public void givenTodoCreationRequest_whenAddTodo_thenTodoIsCreated() throws Exception {
        //given
        TodoCreationRequest todoCreationRequest = new TodoCreationRequest("Tarea 1", false, LocalDate.of(2023, 5, 25));
        String requestBody = objectMapper.writeValueAsString(todoCreationRequest);
        //when
        Todo expectedTodo = new Todo(uuidHelper.generateRandomUUID(), todoCreationRequest.getText(), todoCreationRequest.isFinished(), LocalDate.now(), todoCreationRequest.getExpireDate());
        when(todoService.addTodo(any(TodoCreation.class))).thenReturn(expectedTodo);
        RequestBuilder request = MockMvcRequestBuilders
                .post("/todos")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody)
                .characterEncoding(StandardCharsets.UTF_8);
        //then
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.text").value(expectedTodo.getText()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.creationDate").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.expireDate").value(String.valueOf(expectedTodo.getExpireDate())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.finished").value(expectedTodo.isFinished()))
                .andReturn();
    }


    @Test
    @DisplayName("GetTodo")

    public void givenIdParam_whenGetTodo_thenListTodo() throws Exception {
        //given
        UUID id = UUID.fromString("af690230-84f3-45ea-a960-fc3d4bc99f65");
        //when
        Todo expectedTodo = new Todo(id, "Tarea de prueba 1", false, LocalDate.parse("2023-06-08"), LocalDate.parse("2023-06-01"));
        when(todoService.getTodo(any(UUID.class))).thenReturn(expectedTodo);
        RequestBuilder request = MockMvcRequestBuilders
                .get("/todos/" + id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding(StandardCharsets.UTF_8);
        //then
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.text").value(expectedTodo.getText()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.creationDate").value(String.valueOf(expectedTodo.getCreationDate())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.expireDate").value(String.valueOf(expectedTodo.getExpireDate())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.finished").value(expectedTodo.isFinished()))
                .andReturn();
    }


    @Test
    @DisplayName("UpdateTodo")
    public void givenIdParam_whenUpdateTodo_thenModifyIsFinished() throws Exception {
        //given
        UUID id = UUID.fromString("af690230-84f3-45ea-a960-fc3d4bc99f65");
        TodoUpdateRequest todoUpdateRequest = new TodoUpdateRequest("Tarea actualizada 1", true, LocalDate.now());
        String requestBody = objectMapper.writeValueAsString(todoUpdateRequest);
        Todo expectedTodo = new Todo(id, todoUpdateRequest.getText(), todoUpdateRequest.isFinished(), LocalDate.now(), todoUpdateRequest.getExpireDate());
        //when
        when(todoService.updateTodo(any(UUID.class), any(TodoUpdate.class))).thenReturn(expectedTodo);
        RequestBuilder request = MockMvcRequestBuilders
                .put("/todos/" + id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody)
                .characterEncoding(StandardCharsets.UTF_8);
        //then
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.text").value(expectedTodo.getText()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.creationDate").value(String.valueOf(expectedTodo.getCreationDate())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.expireDate").value(String.valueOf(expectedTodo.getExpireDate())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.finished").value(expectedTodo.isFinished()))
                .andReturn();

    }

    /*
        TESTING CASOS DE ERROR
    */

    @ParameterizedTest
    @MethodSource("provideFieldsForAdd")
    @DisplayName("Casos de error AddTodo")
    public void givenWrongValues_whenAddTodo_thenThrowMethodArgumentNotValidException(String text, boolean isFinished) throws Exception {
        //given
        TodoCreationRequest wrongTodoCreationRequest = new TodoCreationRequest(text, isFinished, LocalDate.of(2023, 5, 25));
        String wrongRequestBody = objectMapper.writeValueAsString(wrongTodoCreationRequest);
        //when
        when(todoService.addTodo(any(TodoCreation.class))).thenReturn(null);
        RequestBuilder request = MockMvcRequestBuilders
                .post("/todos")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding(StandardCharsets.UTF_8.name())
                .content(wrongRequestBody);
        //then
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
    @DisplayName("Casos de error AddTodo")
    public void givenWrongIdParam_whenGetTodo_thenThrowResponseStatusException() throws Exception {
        //given
        UUID invalidId = uuidHelper.generateRandomUUID();
        //when
        when(todoService.getTodo(invalidId)).thenReturn(null);
        RequestBuilder request = MockMvcRequestBuilders
                .get("/todos/" + invalidId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE);
        //then
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof ResponseStatusException))
                .andReturn();
    }

    @Test
    @DisplayName("Casos de error UpdateTodo (Not found)")
    public void givenWrongIdParam_whenUpdateTodo_thenThrowResponseStatusException() throws Exception {
        //given
        UUID invalidId = uuidHelper.generateRandomUUID();
        TodoUpdateRequest todoUpdateRequest = new TodoUpdateRequest("Tarea 1", true, LocalDate.of(2023, 5, 25));
        String requestBody = objectMapper.writeValueAsString(todoUpdateRequest);
        //when
        when(todoService.updateTodo(eq(invalidId), any(TodoUpdate.class))).thenReturn(null);
        RequestBuilder request = MockMvcRequestBuilders
                .put("/todos/" + invalidId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding(StandardCharsets.UTF_8.name())
                .content(requestBody);
        //then
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof ResponseStatusException))
                .andReturn();
    }

    @ParameterizedTest
    @MethodSource("provideFieldsForUpdate")
    @DisplayName("Casos de error UpdateTodo (NotValid)")
    public void givenWrongValues_whenUpdate_thenThrowMethodArgumentNotValidException(String text) throws Exception {
        //given
        UUID id = uuidHelper.generateRandomUUID();
        TodoUpdateRequest wrongTodoUpdateRequest = new TodoUpdateRequest(text, true, LocalDate.of(2023, 5, 25));
        String wrongRequestBody = objectMapper.writeValueAsString(wrongTodoUpdateRequest);
        //when
        when(todoService.updateTodo(eq(id), any(TodoUpdate.class))).thenReturn(null);
        RequestBuilder request = MockMvcRequestBuilders
                .put("/todos/" + id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding(StandardCharsets.UTF_8.name())
                .content(wrongRequestBody);
        //then
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
