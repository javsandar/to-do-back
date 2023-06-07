package com.example.demo.Repository.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "Todo")
public class TodoEntityJpa {
    @Id
    @Column(nullable = false)
    private UUID id;
    @Column(nullable = false)
    private String text;
    @Column(nullable = false)
    private boolean isFinished;

    @Column(nullable = false)
    private LocalDate creationDate;

    @Column(nullable = false)
    private LocalDate expireDate;

    public TodoEntityJpa() {

    }

    public TodoEntityJpa(UUID id, String text, Boolean isFinished, LocalDate creationDate, LocalDate expireDate) {
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
        return "TodoEntity{" + "id=" + id + ", text='" + text + '\'' + ", isFinished=" + isFinished + ", creationDate=" + creationDate + ", expireDate=" + expireDate + '}';
    }
}