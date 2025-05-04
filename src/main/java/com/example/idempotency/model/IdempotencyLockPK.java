package com.example.idempotency.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class IdempotencyLockPK implements Serializable {

    private String processId;
    private String idempotencyKey;

    // Constructors
    public IdempotencyLockPK() {
    }

    public IdempotencyLockPK(String processId, String idempotencyKey) {
        this.processId = processId;
        this.idempotencyKey = idempotencyKey;
    }

    // Getters and setters
    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getIdempotencyKey() {
        return idempotencyKey;
    }

    public void setIdempotencyKey(String idempotencyKey) {
        this.idempotencyKey = idempotencyKey;
    }

    // equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IdempotencyLockPK)) return false;
        IdempotencyLockPK that = (IdempotencyLockPK) o;
        return Objects.equals(processId, that.processId) &&
                Objects.equals(idempotencyKey, that.idempotencyKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(processId, idempotencyKey);
    }
}

