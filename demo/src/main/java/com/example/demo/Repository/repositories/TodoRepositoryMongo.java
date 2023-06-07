package com.example.demo.Repository.repositories;

import com.example.demo.Repository.entities.TodoEntityMongo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface TodoRepositoryMongo extends MongoRepository<TodoEntityMongo, UUID> {
}
