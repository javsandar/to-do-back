package com.example.demo.Service.service;

import com.example.demo.Repository.entities.TodoEntity;
import com.example.demo.Controller.models.TodoPutRequest;
import com.example.demo.Controller.models.TodoRequest;
import com.example.demo.Repository.repositories.TodoRepository;
import com.example.demo.Service.helpers.UUIDhelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TodoService {
    @Autowired
    private TodoRepository todoRepository;

    /**
     * Metodo para filtrar eventos pendientes y no pendientes, se pasa la variable booleana finished
     * como Boolean debido a que como boolean se inicializa por defecto.
     *
     * @param finished
     * @return
     */
    public List<TodoEntity> todosHandler(Boolean finished) {
        if (finished == null) {
            return getAllTodos();
        } else if (!finished) {
            return getRemainingTodos();
        }
        return null;
    }

    public List<TodoEntity> getRemainingTodos() {
        return todoRepository.findRemainingTodos();
    }

    public List<TodoEntity> getAllTodos() {
        return todoRepository.findAll();
    }

    public TodoEntity addTodo(TodoRequest todoRequest) {
        TodoEntity todoEntity = new TodoEntity(UUIDhelper.generateRandomUUID(), todoRequest.getText(), todoRequest.isFinished());
        return todoRepository.save(todoEntity);
    }

    public Optional<TodoEntity> getTodo(UUID id) {
        return todoRepository.findById(id);
    }

    public TodoEntity updateTodo(UUID id, TodoPutRequest todoRequest) {
        TodoEntity updatedTodo = todoRepository.findById(id).get();
        updatedTodo.setText(todoRequest.getText());
        updatedTodo.setFinished(todoRequest.isFinished());
        return todoRepository.save(updatedTodo);
    }

//    public void deleteTodo(UUID id) {
//        todoRepository.getTodos().removeIf(t -> t.getId() == id);
//    }
}
