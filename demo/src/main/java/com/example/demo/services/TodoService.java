package com.example.demo.services;

import com.example.demo.adapters.TodoAdapter;
import com.example.demo.helpers.UUIDhelper;
import com.example.demo.models.Todo;
import com.example.demo.repositories.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TodoService {
    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private TodoAdapter todoAdapter;

    /**
     * Metodo para filtrar eventos pendientes y no pendientes, se pasa la variable booleana finished
     * como Boolean debido a que como boolean se inicializa por defecto.
     *
     * @param finished
     * @return
     */
    public List<Todo> todosHandler(Boolean finished) {
        if (finished == null) {
            return getAllTodos();
        } else if (!finished) {
            return getRemainingTodos();
        }
        return null;
    }

    public List<Todo> getRemainingTodos() {
        return todoRepository.getTodos().stream().filter(todo -> !todo.isFinished()).toList();
    }

    public List<Todo> getAllTodos() {
        return todoRepository.getTodos();
    }

    public Todo addTodo(Todo todo) {
        todo.setId(UUIDhelper.generateRandomUUID());
        return todoAdapter.add(todo);
    }

    public Todo getTodo(UUID id) {
        return todoRepository.getTodos().stream().filter(t -> t.getId().equals(id)).findFirst().get();
    }

    public Todo updateTodoBoolean(UUID id) {
        return todoAdapter.update(id);
    }

//    public void deleteTodo(UUID id) {
//        todoRepository.getTodos().removeIf(t -> t.getId() == id);
//    }
}
