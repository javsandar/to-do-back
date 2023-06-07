package com.example.demo.Repository.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.util.UUID;

@Document("todos")
public class TodoEntityMongo {
    @Id
    private UUID id;
    @Field("text")
    private String text;
    @Field("isFinished")
    private boolean isFinished;
    @Field("creationDate")
    private LocalDate creationDate;
    @Field("expireDate")
    private LocalDate expireDate;

    public TodoEntityMongo() {

    }

    public TodoEntityMongo(UUID id, String text, Boolean isFinished, LocalDate creationDate, LocalDate expireDate) {
        this.id = id;
        this.text = text;
        this.isFinished = isFinished;
        this.creationDate = creationDate;
        this.expireDate = expireDate;
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

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDate getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(LocalDate expireDate) {
        this.expireDate = expireDate;
    }

    @Override
    public String toString() {
        return "TodoEntityMongo{" + "id=" + id + ", text='" + text + '\'' + ", isFinished=" + isFinished + ", creationDate=" + creationDate + ", expireDate=" + expireDate + '}';
    }
}
