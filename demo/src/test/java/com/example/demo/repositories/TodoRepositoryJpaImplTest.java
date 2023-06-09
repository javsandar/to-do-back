package com.example.demo.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.Predicate;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.stream.Stream;

@SpringBootTest
@TestPropertySource(locations = "/data/repositories/h2/db-test.properties")
@Sql("/data/repositories/h2/test-mysql.sql")
public class TodoRepositoryJpaImplTest {
    @Autowired
    private TodoRepositoryJpa todoRepository;
    @Autowired
    private static EntityManager em;

    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @BeforeEach
    public void setTodoRepository() throws Exception {
        System.out.println("----------------------TODOS----------------------");
        todoRepository.findAll().forEach(System.out::println);
        System.out.println("-------------------------------------------------");
    }

    @ParameterizedTest
    @MethodSource("predicatesProvider")
    @DisplayName("findTodosByFilter")
    public void givenTodoEntityFilter_whenFindTodosByFilter_thenReturnFilteredTodos(List<Predicate> predicates) {
        //given
        //when
        //then
    }

    private static Stream<Arguments> predicatesProvider() {

        return Stream.of(
                Arguments.of(null)
        );
    }
}