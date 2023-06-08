package com.example.demo.Repository.repositories;

import com.example.demo.Repository.entities.TodoEntityMongo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TodoRepositoryMongo extends MongoRepository<TodoEntityMongo, UUID> {
}
