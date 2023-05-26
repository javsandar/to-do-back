package com.example.demo.Service.models;

import java.time.LocalDate;

public class TodoUpdateDto {
    private String text;
    private boolean isFinished;

    private LocalDate expireDate;

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
        return "TodoUpdateDto{" +
                "text='" + text + '\'' +
                ", isFinished=" + isFinished +
                ", expireDate=" + expireDate +
                '}';
    }
}
