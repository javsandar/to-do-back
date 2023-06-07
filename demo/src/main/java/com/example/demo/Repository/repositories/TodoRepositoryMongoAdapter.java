package com.example.demo.Repository.repositories;

import com.example.demo.Repository.entities.TodoEntityMongo;
import com.example.demo.Repository.models.TodoEntityFilter;
import com.example.demo.Service.models.Todo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Primary
@Component
public class TodoRepositoryMongoAdapter implements TodoRepository {
    @Autowired
    private TodoRepositoryMongo todoRepositoryMongo;
    @Autowired
    private MongoTemplate mongoTemplate;
    private ModelMapper modelMapper = new ModelMapper();

    @Override
    public List<Todo> findTodosByFilter(TodoEntityFilter todoEntityFilter) {
        Query query = new Query();

        List<Criteria> criterias = new ArrayList<>();
        if (todoEntityFilter.getFinished() != null) {
            criterias.add(Criteria.where("isFinished").is(todoEntityFilter.getFinished()));
        }
        if (todoEntityFilter.getCreationDate() != null) {
            criterias.add(Criteria.where("creationDate").is(todoEntityFilter.getCreationDate()));
        }
        if (todoEntityFilter.getExpireDate() != null) {
            if (todoEntityFilter.getExpireDate().size() == 1 & !todoEntityFilter.getExpireDate().get(0).contains(":")) {
                System.out.println(todoEntityFilter.getExpireDate().get(0));
                criterias.add(Criteria.where("expireDate").is(LocalDate.parse(todoEntityFilter.getExpireDate().get(0))));
            }
            if (todoEntityFilter.getExpireDate().size() == 2 | (todoEntityFilter.getExpireDate().size() == 1 & todoEntityFilter.getExpireDate().get(0).contains(":"))) {
                for (String date : todoEntityFilter.getExpireDate()) {
                    String[] array = date.split(":");
                    String operator = array[0];
                    LocalDate expireDate = LocalDate.parse(array[1]);
                    if (operator.equals("gte")) {
                        criterias.add(Criteria.where("expireDate").gte(expireDate));
                    }
                    if (operator.equals("lte")) {
                        criterias.add(Criteria.where("expireDate").lte(expireDate));
                    }
                    if (operator.equals("gt")) {
                        criterias.add(Criteria.where("expireDate").gt(expireDate));
                    }
                    if (operator.equals("lt")) {
                        criterias.add(Criteria.where("expireDate").lt(expireDate));
                    }
                }
            }
        }
        for (Criteria criteria : criterias) {
            query.addCriteria(criteria);
        }
        List<TodoEntityMongo> queryList = mongoTemplate.find(query, TodoEntityMongo.class);
        List<Todo> result = queryList.stream().map(element -> modelMapper.map(element, Todo.class)).toList();
        return result;
    }

    @Override
    public Todo save(Todo todo) {
        TodoEntityMongo todoEntityMongo = modelMapper.map(todo, TodoEntityMongo.class);
        TodoEntityMongo updatedTodo = todoRepositoryMongo.save(todoEntityMongo);
        Todo result = modelMapper.map(updatedTodo, Todo.class);
        return result;
    }

    @Override
    public Optional<Todo> findById(UUID id) {
        Optional<TodoEntityMongo> todoEntityMongo = todoRepositoryMongo.findById(id);
        Optional<Todo> result = Optional.ofNullable(modelMapper.map(todoEntityMongo, Todo.class));
        return result;
    }

}
