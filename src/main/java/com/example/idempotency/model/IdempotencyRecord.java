package com.example.idempotency.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "idempotency_record")
public class IdempotencyRecord {

    @Id
    private String id;

    private Instant createdAt;

    private String data;

    // Constructors
    public IdempotencyRecord() {
    }

    public IdempotencyRecord(String id, Instant createdAt, String data) {
        this.id = id;
        this.createdAt = createdAt;
        this.data = data;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}

