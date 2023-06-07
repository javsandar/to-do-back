//package com.example.demo.Repository.repositories;
//
//import com.example.demo.Repository.entities.TodoEntity;
//import com.example.demo.Repository.models.TodoEntityFilter;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.criteria.*;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.Arguments;
//import org.junit.jupiter.params.provider.MethodSource;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.time.LocalDate;
//import java.util.*;
//import java.util.stream.Stream;
//
//import static org.junit.Assert.assertEquals;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.*;
//
//public class TodoRepositoryJpaImplTest {
//    @Mock
//    private TodoRepositoryCustom todoRepository = new TodoRepositoryJpaImpl();
//    @Mock
//    private EntityManager entityManager;
//    @Mock
//    private CriteriaQuery<TodoEntity> criteriaQuery;
//    @Mock
//    private Root<TodoEntity> todoEntityRoot;
//    @Mock
//    private CriteriaBuilder criteriaBuilder;
//    @BeforeEach
//    public void init() {
//        MockitoAnnotations.openMocks(this);
//
//    }
//
//
//    @ParameterizedTest
//    @MethodSource("predicatesProvider")
//    public void givenTodoEntityFilter_whenFindTodosByFilter_thenReturnFilteredTodos(TodoEntityFilter filter, List<TodoEntity> expectedTodos) throws InstantiationException, IllegalAccessException {
//        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
//        when(criteriaBuilder.createQuery(TodoEntity.class)).thenReturn(criteriaQuery);
//        when(criteriaQuery.from(TodoEntity.class)).thenReturn(todoEntityRoot);
//
//
//    }
//
//    private static Stream<Arguments> predicatesProvider() {
//        TodoEntityFilter filterAllNull = new TodoEntityFilter(null, null, null);
//        TodoEntityFilter filterFinished = new TodoEntityFilter(true, null, null);
//        TodoEntityFilter filterCreationDate = new TodoEntityFilter(null, LocalDate.parse("2023-06-01"), null);
//        TodoEntityFilter filterExpireDate = new TodoEntityFilter(null, null, Collections.singletonList("2023-05-31"));
//        TodoEntityFilter filterFinishedAndExpireDate = new TodoEntityFilter(false, null, Collections.singletonList("2023-05-31"));
//        TodoEntityFilter filterGreaterThanEqualExpireDate = new TodoEntityFilter(null, null, Collections.singletonList("gte:2023-05-25"));
//        TodoEntityFilter filterGreaterThanExpireDate = new TodoEntityFilter(null, null, Collections.singletonList("gt:2023-05-25"));
//        TodoEntityFilter filterLessThanEqualExpireDate = new TodoEntityFilter(null, null, Collections.singletonList("lte:2023-05-31"));
//        TodoEntityFilter filterLessThanExpireDate = new TodoEntityFilter(null, null, Collections.singletonList("lt:2023-05-31"));
//        TodoEntityFilter filterGreaterThanEqualExpireDateAndLessThanEqualExpireDate = new TodoEntityFilter(null, null, Arrays.asList("gte:2023-05-25", "lte:2023-05-31"));
//        TodoEntityFilter filterGreaterThanEqualExpireDateAndLessThanExpireDate = new TodoEntityFilter(null, null, Arrays.asList("gte:2023-05-25", "lt:2023-05-31"));
//        TodoEntityFilter filterGreaterThanExpireDateAndLessThanEqualExpireDate = new TodoEntityFilter(null, null, Arrays.asList("gt:2023-05-25", "lte:2023-05-31"));
//        TodoEntityFilter filterGreaterThanExpireDateAndLessThanExpireDate = new TodoEntityFilter(null, null, Arrays.asList("gt:2023-05-25", "lt:2023-05-31"));
//
//        List<TodoEntity> caseAllNull = List.of(
//                new TodoEntity(UUID.fromString("72836dd4-ec1a-4ff6-98f3-55bfeb2728d3"), "Tarea de prueba 0", false, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-24")),
//                new TodoEntity(UUID.fromString("72836dd4-ec1a-4ff6-98f3-55bfeb2728d5"), "Tarea de prueba 1", false, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-25")),
//                new TodoEntity(UUID.fromString("39431462-9fbe-425f-96ba-e45f0bc67e71"), "Tarea de prueba 2", false, LocalDate.parse("2023-05-25"), LocalDate.parse("2023-05-26")),
//                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940c"), "Tarea de prueba 3", true, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-27")),
//                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940e"), "Tarea de prueba 5", false, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-28")),
//                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940d"), "Tarea de prueba 4", false, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-30")),
//                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940f"), "Tarea de prueba 6", false, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-31")),
//                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4941c"), "Tarea de prueba 7", true, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-06-01"))
//        );
//        List<TodoEntity> caseFinished = List.of(
//                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940c"), "Tarea de prueba 3", true, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-27")),
//                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4941c"), "Tarea de prueba 7", true, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-06-01"))
//        );
//        List<TodoEntity> caseCreationDate = List.of(
//                new TodoEntity(UUID.fromString("39431462-9fbe-425f-96ba-e45f0bc67e71"), "Tarea de prueba 2", false, LocalDate.parse("2023-06-01"), LocalDate.parse("2023-05-26"))
//        );
//        List<TodoEntity> caseExpireDate = List.of(
//                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940f"), "Tarea de prueba 6", true, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-31"))
//        );
//        List<TodoEntity> caseFinishedAndExpireDate = List.of(
//                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940f"), "Tarea de prueba 6", false, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-31"))
//        );
//        List<TodoEntity> caseGreaterThanEqualExpireDate = List.of(
//                new TodoEntity(UUID.fromString("72836dd4-ec1a-4ff6-98f3-55bfeb2728d5"), "Tarea de prueba 1", false, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-25")),
//                new TodoEntity(UUID.fromString("39431462-9fbe-425f-96ba-e45f0bc67e71"), "Tarea de prueba 2", false, LocalDate.parse("2023-05-25"), LocalDate.parse("2023-05-26")),
//                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940c"), "Tarea de prueba 3", true, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-27")),
//                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940e"), "Tarea de prueba 5", false, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-28")),
//                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940d"), "Tarea de prueba 4", false, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-30")),
//                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940f"), "Tarea de prueba 6", false, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-31")),
//                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4941c"), "Tarea de prueba 7", true, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-06-01"))
//        );
//        List<TodoEntity> caseGreaterThanExpireDate = List.of(
//                new TodoEntity(UUID.fromString("39431462-9fbe-425f-96ba-e45f0bc67e71"), "Tarea de prueba 2", false, LocalDate.parse("2023-05-25"), LocalDate.parse("2023-05-26")),
//                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940c"), "Tarea de prueba 3", true, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-27")),
//                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940e"), "Tarea de prueba 5", false, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-28")),
//                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940d"), "Tarea de prueba 4", false, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-30")),
//                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940f"), "Tarea de prueba 6", false, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-31")),
//                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4941c"), "Tarea de prueba 7", true, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-06-01"))
//        );
//        List<TodoEntity> caseLessThanEqualExpireDate = List.of(
//                new TodoEntity(UUID.fromString("72836dd4-ec1a-4ff6-98f3-55bfeb2728d3"), "Tarea de prueba 0", false, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-24")),
//                new TodoEntity(UUID.fromString("72836dd4-ec1a-4ff6-98f3-55bfeb2728d5"), "Tarea de prueba 1", false, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-25")),
//                new TodoEntity(UUID.fromString("39431462-9fbe-425f-96ba-e45f0bc67e71"), "Tarea de prueba 2", false, LocalDate.parse("2023-05-25"), LocalDate.parse("2023-05-26")),
//                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940c"), "Tarea de prueba 3", true, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-27")),
//                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940e"), "Tarea de prueba 5", false, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-28")),
//                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940d"), "Tarea de prueba 4", false, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-30")),
//                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940f"), "Tarea de prueba 6", false, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-31"))
//        );
//        List<TodoEntity> caseLessThanExpireDate = List.of(
//                new TodoEntity(UUID.fromString("72836dd4-ec1a-4ff6-98f3-55bfeb2728d3"), "Tarea de prueba 0", false, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-24")),
//                new TodoEntity(UUID.fromString("72836dd4-ec1a-4ff6-98f3-55bfeb2728d5"), "Tarea de prueba 1", false, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-25")),
//                new TodoEntity(UUID.fromString("39431462-9fbe-425f-96ba-e45f0bc67e71"), "Tarea de prueba 2", false, LocalDate.parse("2023-05-25"), LocalDate.parse("2023-05-26")),
//                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940c"), "Tarea de prueba 3", true, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-27")),
//                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940e"), "Tarea de prueba 5", false, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-28")),
//                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940d"), "Tarea de prueba 4", false, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-30"))
//        );
//        List<TodoEntity> caseGreaterThanEqualExpireDateAndLessThanEqualExpireDate = List.of(
//                new TodoEntity(UUID.fromString("72836dd4-ec1a-4ff6-98f3-55bfeb2728d5"), "Tarea de prueba 1", false, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-25")),
//                new TodoEntity(UUID.fromString("39431462-9fbe-425f-96ba-e45f0bc67e71"), "Tarea de prueba 2", false, LocalDate.parse("2023-05-25"), LocalDate.parse("2023-05-26")),
//                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940c"), "Tarea de prueba 3", true, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-27")),
//                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940e"), "Tarea de prueba 5", false, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-28")),
//                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940d"), "Tarea de prueba 4", false, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-30")),
//                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940f"), "Tarea de prueba 6", false, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-31"))
//        );
//        List<TodoEntity> caseGreaterThanEqualExpireDateAndLessThanExpireDate = List.of(
//                new TodoEntity(UUID.fromString("72836dd4-ec1a-4ff6-98f3-55bfeb2728d5"), "Tarea de prueba 1", false, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-25")),
//                new TodoEntity(UUID.fromString("39431462-9fbe-425f-96ba-e45f0bc67e71"), "Tarea de prueba 2", false, LocalDate.parse("2023-05-25"), LocalDate.parse("2023-05-26")),
//                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940c"), "Tarea de prueba 3", true, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-27")),
//                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940e"), "Tarea de prueba 5", false, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-28")),
//                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940d"), "Tarea de prueba 4", false, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-30"))
//        );
//        List<TodoEntity> caseGreaterThanExpireDateAndLessThanEqualExpireDate = List.of(
//                new TodoEntity(UUID.fromString("39431462-9fbe-425f-96ba-e45f0bc67e71"), "Tarea de prueba 2", false, LocalDate.parse("2023-05-25"), LocalDate.parse("2023-05-26")),
//                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940c"), "Tarea de prueba 3", true, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-27")),
//                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940e"), "Tarea de prueba 5", false, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-28")),
//                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940d"), "Tarea de prueba 4", false, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-30")),
//                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940f"), "Tarea de prueba 6", false, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-31"))
//        );
//        List<TodoEntity> caseGreaterThanExpireDateAndLessThanExpireDate = List.of(
//                new TodoEntity(UUID.fromString("39431462-9fbe-425f-96ba-e45f0bc67e71"), "Tarea de prueba 2", false, LocalDate.parse("2023-05-25"), LocalDate.parse("2023-05-26")),
//                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940c"), "Tarea de prueba 3", true, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-27")),
//                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940e"), "Tarea de prueba 5", false, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-28")),
//                new TodoEntity(UUID.fromString("4fb21f20-28d9-4af1-a20c-ab364ba4940d"), "Tarea de prueba 4", false, LocalDate.parse("2023-05-24"), LocalDate.parse("2023-05-30"))
//
//        );
//        return Stream.of(
//                Arguments.of(filterAllNull, caseAllNull),
//                Arguments.of(filterFinished, caseFinished),
//                Arguments.of(filterCreationDate, caseCreationDate),
//                Arguments.of(filterExpireDate, caseExpireDate),
//                Arguments.of(filterFinishedAndExpireDate, caseFinishedAndExpireDate),
//                Arguments.of(filterGreaterThanEqualExpireDate, caseGreaterThanEqualExpireDate),
//                Arguments.of(filterGreaterThanExpireDate, caseGreaterThanExpireDate),
//                Arguments.of(filterLessThanEqualExpireDate, caseLessThanEqualExpireDate),
//                Arguments.of(filterLessThanExpireDate, caseLessThanExpireDate),
//                Arguments.of(filterGreaterThanEqualExpireDateAndLessThanEqualExpireDate, caseGreaterThanEqualExpireDateAndLessThanEqualExpireDate),
//                Arguments.of(filterGreaterThanEqualExpireDateAndLessThanExpireDate, caseGreaterThanEqualExpireDateAndLessThanExpireDate),
//                Arguments.of(filterGreaterThanExpireDateAndLessThanEqualExpireDate, caseGreaterThanExpireDateAndLessThanEqualExpireDate),
//                Arguments.of(filterGreaterThanExpireDateAndLessThanExpireDate, caseGreaterThanExpireDateAndLessThanExpireDate)
//        );
//    }
//
//
//}
