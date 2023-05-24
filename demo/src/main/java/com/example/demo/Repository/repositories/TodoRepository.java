package com.example.demo.Repository.repositories;

import com.example.demo.Repository.entities.TodoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface TodoRepository extends JpaRepository<TodoEntity, UUID> {
}
