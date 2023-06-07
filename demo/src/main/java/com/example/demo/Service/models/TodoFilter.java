package com.example.demo.Service.models;

import java.time.LocalDate;
import java.util.List;

public class TodoFilter {
    private Boolean finished;
    private LocalDate creationDate;

    private List<String> expireDate;
    public TodoFilter(){

    }

    public TodoFilter(Boolean finished, LocalDate creationDate, List<String> expireDate) {
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

    public List<String> getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(List<String> expireDate) {
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
