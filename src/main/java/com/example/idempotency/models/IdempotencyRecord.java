package com.example.idempotency.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "idempotency_record", indexes = @Index(name = "idempotency_record_created_at", columnList = "created_at"))
public class IdempotencyRecord {

    @Id
    @Column(name = "id", length = 1024)
    private String id;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "data", nullable = false, columnDefinition = "TEXT")
    private String data;

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}

