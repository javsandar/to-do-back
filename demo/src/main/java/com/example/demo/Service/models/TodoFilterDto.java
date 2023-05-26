package com.example.demo.Service.models;

import java.time.LocalDate;

public class TodoFilterDto {
    private Boolean finished;
    private LocalDate creationDate;

    private LocalDate expireDate;
    public TodoFilterDto(){

    }

    public TodoFilterDto(Boolean finished, LocalDate creationDate, LocalDate expireDate) {
        this.finished = finished;
        this.creationDate = creationDate;
        this.expireDate = expireDate;
    }

    public Boolean getFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
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
        return "TodoFilterDto{" +
                "finished=" + finished +
                ", creationDate=" + creationDate +
                ", expireDate=" + expireDate +
                '}';
    }
}
