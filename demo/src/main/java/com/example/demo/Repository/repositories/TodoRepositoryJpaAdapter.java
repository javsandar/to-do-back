package com.example.demo.Repository.repositories;

import com.example.demo.Repository.entities.TodoEntityJpa;
import com.example.demo.Repository.models.TodoEntityFilter;
import com.example.demo.Service.models.Todo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

//@Primary
@Component
public class TodoRepositoryJpaAdapter implements TodoRepository {
    @Autowired
    private TodoRepositoryJpa todoRepositoryJpa;
    @Autowired
    private EntityManager em;
    private ModelMapper modelMapper = new ModelMapper();

    @Override
    public List<Todo> findTodosByFilter(TodoEntityFilter todoEntityFilter) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<TodoEntityJpa> cq = cb.createQuery(TodoEntityJpa.class);
        Root<TodoEntityJpa> todoEntityJpa = cq.from(TodoEntityJpa.class);

        List<Predicate> predicates = new ArrayList<>();
        if (todoEntityFilter.getFinished() != null) {
            predicates.add(cb.equal(todoEntityJpa.get("isFinished"), todoEntityFilter.getFinished()));
        }
        if (todoEntityFilter.getCreationDate() != null) {
            predicates.add(cb.equal(todoEntityJpa.get("creationDate"), todoEntityFilter.getCreationDate()));
        }
        if (todoEntityFilter.getExpireDate() != null) {
            for (String date : todoEntityFilter.getExpireDate()) {
                if (date.contains(":")) {
                    String[] array = date.split(":");
                    String operator = array[0];
                    LocalDate expireDate = LocalDate.parse(array[1]);
                    if ("gte".equals(operator)) {
                        predicates.add(cb.greaterThanOrEqualTo(todoEntityJpa.get("expireDate"), expireDate));
                    }
                    if ("lte".equals(operator)) {
                        predicates.add(cb.lessThanOrEqualTo(todoEntityJpa.get("expireDate"), expireDate));
                    }
                    if ("gt".equals(operator)) {
                        predicates.add(cb.greaterThan(todoEntityJpa.get("expireDate"), expireDate));
                    }
                    if ("lt".equals(operator)) {
                        predicates.add(cb.lessThan(todoEntityJpa.get("expireDate"), expireDate));
                    }
                } else {
                    predicates.add(cb.equal(todoEntityJpa.get("expireDate"), LocalDate.parse(date)));
                }
            }
        }
        cq.where(predicates.toArray(new Predicate[0]));
        List<TodoEntityJpa> queryList = em.createQuery(cq).getResultList();
        List<Todo> result = queryList.stream().map(element -> modelMapper.map(element, Todo.class)).toList();
        return result;
    }

    @Override
    public Todo save(Todo todo) {
        TodoEntityJpa todoToJpa = modelMapper.map(todo, TodoEntityJpa.class);
        TodoEntityJpa todoEntityJpa = todoRepositoryJpa.save(todoToJpa);
        Todo result = modelMapper.map(todoEntityJpa, Todo.class);
        return result;
    }

    @Override
    public Optional<Todo> findById(UUID id) {
        Optional<TodoEntityJpa> todoEntityJpa = todoRepositoryJpa.findById(id);
        Optional<Todo> result = Optional.ofNullable(modelMapper.map(todoEntityJpa, Todo.class));
        return result;
    }
}
