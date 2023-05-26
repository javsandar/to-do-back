package com.example.demo.models;

import java.time.LocalDate;

public class TodoFilterDTO {
    private Boolean finished;
    private LocalDate creationDate;

    private LocalDate expireDate;

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
}
