package com.example.demo.Controller.models;

import jakarta.validation.constraints.AssertFalse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import nonapi.io.github.classgraph.json.Id;

import java.util.Objects;
import java.util.UUID;

public class Todo {
    @Id
    private UUID id;
    @NotBlank(message = "Todo description can not be empty")
    @Size(max = 20, message = "Todo text can not be greater than 20 characters")
    private String text;
    @AssertFalse(message = "Todo can not be finished")
    private boolean isFinished;

    public Todo() {

    }

    public Todo(String text, boolean isFinished) {
        this.text = text;
        this.isFinished = isFinished;
    }

    public Todo(UUID id, String text, boolean isFinished) {
        this.id = id;
        this.text = text;
        this.isFinished = isFinished;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Todo todo = (Todo) o;
        return id == todo.id && isFinished == todo.isFinished && Objects.equals(text, todo.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text, isFinished);
    }

    @Override
    public String toString() {
        return "Todo{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", isFinished=" + isFinished +
                '}';
    }
}
