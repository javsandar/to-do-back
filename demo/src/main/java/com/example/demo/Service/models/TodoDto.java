package com.example.demo.Service.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.time.LocalDate;
import java.util.UUID;

public class TodoDto {
    private UUID id;
    private String text;
    private boolean isFinished;
    private LocalDate creationDate;
    private LocalDate expireDate;

    public TodoDto() {

    }

    public TodoDto(UUID id, String text, boolean isFinished, LocalDate creationDate, LocalDate expireDate) {
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
        return "TodoDto{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", isFinished=" + isFinished +
                ", creationDate=" + creationDate +
                ", expireDate=" + expireDate +
                '}';
    }
}
