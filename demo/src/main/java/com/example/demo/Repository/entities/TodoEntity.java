package com.example.demo.Repository.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "Todo")
public class TodoEntity {
    @Id
    @Column(nullable = false)
    private UUID id;
    @Column(nullable = false)
    private String text;
    @Column(nullable = false)
    private boolean isFinished;

    public TodoEntity() {

    }

    public TodoEntity(UUID id, String text, boolean isFinished) {
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
        TodoEntity that = (TodoEntity) o;
        return isFinished == that.isFinished && Objects.equals(id, that.id) && Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text, isFinished);
    }

    @Override
    public String toString() {
        return "TodoEntity{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", isFinished=" + isFinished +
                '}';
    }
}