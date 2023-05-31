package com.example.demo.Repository.repositories;

import com.example.demo.Repository.entities.TodoEntity;
import com.example.demo.Repository.models.TodoEntityFilter;
import com.example.demo.Service.models.TodoFilterDto;

import java.util.List;

interface TodoRepositoryCustom {
    List<TodoEntity> findTodosByFilter(TodoEntityFilter filter);
}