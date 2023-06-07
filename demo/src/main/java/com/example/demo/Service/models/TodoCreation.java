package com.example.demo.Service.models;

import java.time.LocalDate;

public class TodoCreation {
    private String text;
    private boolean isFinished;
    private LocalDate expireDate;

    public TodoCreation() {
    }

    public TodoCreation(String text, boolean isFinished, LocalDate expireDate) {
        this.text = text;
        this.isFinished = isFinished;
        this.expireDate = expireDate;
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

    public LocalDate getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(LocalDate expireDate) {
        this.expireDate = expireDate;
    }

    @Override
    public String toString() {
        return "TodoCreationDto{" +
                "text='" + text + '\'' +
                ", isFinished=" + isFinished +
                ", expireDate=" + expireDate +
                '}';
    }
}
