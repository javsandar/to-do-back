package com.example.demo.Controller.models;

import jakarta.validation.constraints.AssertFalse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TodoRequestModel {
    @NotBlank(message = "Todo description can not be empty")
    @Size(max = 20, message = "Todo text can not be greater than 20 characters")
    private String text;
    @AssertFalse(message = "Todo can not be finished")
    private boolean isFinished;

    public TodoRequestModel() {

    }

    public TodoRequestModel(String text, boolean isFinished) {
        this.text = text;
        this.isFinished = isFinished;
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
    public String toString() {
        return "TodoRequestModel{" +
                "text='" + text + '\'' +
                ", isFinished=" + isFinished +
                '}';
    }
}
