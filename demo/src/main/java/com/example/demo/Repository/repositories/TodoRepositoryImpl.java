package com.example.demo.Repository.repositories;

import com.example.demo.Repository.entities.TodoEntity;
import com.example.demo.Repository.models.TodoEntityFilter;
import com.example.demo.Service.models.TodoFilterDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
class TodoRepositoryImpl implements TodoRepositoryCustom {
    @Autowired
    EntityManager em;
    @Override
    public List<TodoEntity> findTodosByFilter(TodoEntityFilter filter) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<TodoEntity> cq = cb.createQuery(TodoEntity.class);
        Root<TodoEntity> todoEntity = cq.from(TodoEntity.class);
        List<Predicate> predicates = new ArrayList<>();
        if (filter.getFinished() != null){
            predicates.add(cb.equal(todoEntity.get("isFinished"), filter.getFinished()));
        }
        if (filter.getCreationDate() != null){
            predicates.add(cb.equal(todoEntity.get("creationDate"), filter.getCreationDate()));
        }
        if (filter.getExpireDate() != null){
            for (String date : filter.getExpireDate()) {
                if (date.contains(":")){
                    String[] array = date.split(":");
                    String operator = array[0];
                    LocalDate expireDate = LocalDate.parse(array[1]);
                    if(operator.equals("gte")){
                        predicates.add(cb.greaterThanOrEqualTo(todoEntity.get("expireDate"), expireDate));
                    }
                    if(operator.equals("lte")){
                        predicates.add(cb.lessThanOrEqualTo(todoEntity.get("expireDate"), expireDate));
                    }
                    if(operator.equals("gt")){
                        predicates.add(cb.greaterThan(todoEntity.get("expireDate"), expireDate));
                    }
                    if(operator.equals("lt")){
                        predicates.add(cb.lessThan(todoEntity.get("expireDate"), expireDate));
                    }
                } else {
                    predicates.add(cb.equal(todoEntity.get("expireDate"), LocalDate.parse(date)));
                }
            }
        }
        cq.where(predicates.toArray(new Predicate[0]));
        List<TodoEntity> result = em.createQuery(cq).getResultList();
        return result;
     }

}
