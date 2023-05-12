package com.example.demo.Service.service;

import com.example.demo.Repository.entities.TodoEntity;
import com.example.demo.Controller.models.TodoUpdateRequestModel;
import com.example.demo.Controller.models.TodoRequestModel;
import com.example.demo.Repository.repositories.TodoRepository;
import com.example.demo.Service.helpers.UUIDhelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TodoServiceImpl implements TodoService {
    @Autowired
    private TodoRepository todoRepository;

    /**
     * Metodo para filtrar eventos pendientes y no pendientes, se pasa la variable booleana finished
     * como Boolean debido a que como boolean se inicializa por defecto.
     *
     * @param finished
     * @return
     */
    @Override
    public List<TodoEntity> todosHandler(Boolean finished) {
        if (finished == null) {
            return getAllTodos();
        } else if (!finished) {
            return getRemainingTodos(false);
        }
        return null;
    }
    @Override
    public List<TodoEntity> getRemainingTodos(Boolean finished) {
        return todoRepository.findByIsFinished(finished);
    }
    @Override
    public List<TodoEntity> getAllTodos() {
        return todoRepository.findAll();
    }
    @Override
    public TodoEntity addTodo(TodoRequestModel todoRequestModel) {
        TodoEntity todoEntity = new TodoEntity(UUIDhelper.generateRandomUUID(), todoRequestModel.getText(), todoRequestModel.isFinished());
        return todoRepository.save(todoEntity);
    }
    @Override
    public Optional<TodoEntity> getTodo(UUID id) {
        return todoRepository.findById(id);
    }
    @Override
    public Optional<TodoEntity> updateTodo(UUID id, TodoUpdateRequestModel todoUpdateRequestModel) {
        TodoEntity updatedTodo = todoRepository.findById(id).get();
        updatedTodo.setText(todoUpdateRequestModel.getText());
        updatedTodo.setFinished(todoUpdateRequestModel.isFinished());
        return Optional.of(todoRepository.save(updatedTodo));
    }

}
