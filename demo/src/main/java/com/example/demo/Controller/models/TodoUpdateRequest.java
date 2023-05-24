package com.example.demo.Controller.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class TodoUpdateRequest {

    @NotBlank(message = "Todo description can not be empty")
    @Size(max = 20, message = "Todo text can not be greater than 20 characters")
    private String text;
    private boolean isFinished;

    private LocalDate expireDate;


    public TodoUpdateRequest() {

    }

    public TodoUpdateRequest(String text, boolean isFinished, LocalDate expireDate) {
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

}
