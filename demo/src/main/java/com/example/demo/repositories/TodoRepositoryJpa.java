package com.example.demo.repositories;

import com.example.demo.repositories.entities.TodoEntityJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TodoRepositoryJpa extends JpaRepository<TodoEntityJpa, UUID> {
}
