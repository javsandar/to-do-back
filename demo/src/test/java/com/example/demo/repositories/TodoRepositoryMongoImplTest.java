package com.example.demo.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
public class TodoRepositoryMongoImplTest {
    @Autowired
    private TodoRepositoryMongo todoRepository;


}