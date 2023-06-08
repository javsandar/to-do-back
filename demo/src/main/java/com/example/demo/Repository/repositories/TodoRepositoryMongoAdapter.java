package com.example.demo.Repository.repositories;

import com.example.demo.Repository.entities.TodoEntityMongo;
import com.example.demo.Repository.models.TodoEntityFilter;
import com.example.demo.Service.models.Todo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Profile("MongoDB")
@Component("todoRepositoryMongoAdapter")
public class TodoRepositoryMongoAdapter implements TodoRepository {
    @Autowired
    private TodoRepositoryMongo todoRepositoryMongo;
    @Autowired
    private MongoTemplate mongoTemplate;
    private final ModelMapper modelMapper = new ModelMapper();

    @Override
    public List<Todo> findTodosByFilter(TodoEntityFilter todoEntityFilter) {
        ArrayList<List<Criteria>> criteriaList = getCriterias(todoEntityFilter);
        Query query = buildQuery(new Query(), criteriaList);
        List<TodoEntityMongo> queryList = mongoTemplate.find(query, TodoEntityMongo.class);
        List<Todo> result = queryList.stream().map(element -> modelMapper.map(element, Todo.class)).toList();
        return result;
    }

    private Query buildQuery(Query query, ArrayList<List<Criteria>> criteriaList) {
        for (List<Criteria> cList : criteriaList) {
            for (Criteria c : cList) {
                query.addCriteria(c);
            }
        }
        System.out.println(query);
        return query;
    }

    private ArrayList<List<Criteria>> getCriterias(TodoEntityFilter todoEntityFilter) {
        ArrayList<List<Criteria>> criteriaList = new ArrayList<>();
        List<Criteria> criterias = new ArrayList<>();
        List<Criteria> expireDateCriterias = new ArrayList<>();
        if (todoEntityFilter.getFinished() != null) {
            criterias.add(Criteria.where("finished").is(todoEntityFilter.getFinished()));
        }
        if (todoEntityFilter.getCreationDate() != null) {
            criterias.add(Criteria.where("creationDate").is(todoEntityFilter.getCreationDate()));
        }
        if (todoEntityFilter.getExpireDate() != null) {
            if (todoEntityFilter.getExpireDate().size() == 1 & !todoEntityFilter.getExpireDate().get(0).contains(":")) {
                expireDateCriterias.add(Criteria.where("expireDate").is(LocalDate.parse(todoEntityFilter.getExpireDate().get(0))));
            }
            if (todoEntityFilter.getExpireDate().size() == 2 | (todoEntityFilter.getExpireDate().size() == 1 & todoEntityFilter.getExpireDate().get(0).contains(":"))) {
                Criteria c = new Criteria();
                List<Criteria> andCriterias = new ArrayList<>();
                for (String date : todoEntityFilter.getExpireDate()) {
                    String[] array = date.split(":");
                    String operator = array[0];
                    LocalDate expireDate = LocalDate.parse(array[1]);
                    if (operator.equals("gte")) {
                        andCriterias.add(Criteria.where("expireDate").gte(expireDate));
                    }
                    if (operator.equals("lte")) {
                        andCriterias.add(Criteria.where("expireDate").lte(expireDate));
                    }
                    if (operator.equals("gt")) {
                        andCriterias.add(Criteria.where("expireDate").gt(expireDate));
                    }
                    if (operator.equals("lt")) {
                        andCriterias.add(Criteria.where("expireDate").lt(expireDate));
                    }
                }
                if (andCriterias.size() == 2) {
                    c.andOperator(andCriterias.get(0), andCriterias.get(1));
                }
                if (andCriterias.size() == 1) {
                    c.andOperator(andCriterias.get(0));
                }
                expireDateCriterias.add(c);
            }
        }
        criteriaList.add(criterias);
        criteriaList.add(expireDateCriterias);
        return criteriaList;
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
