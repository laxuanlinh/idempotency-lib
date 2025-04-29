package com.example.idempotency.models;

import java.io.Serializable;

public class IdempotencyLockId implements Serializable {

    private String processId;
    private String idempotencyKey;

    // Constructors
    public IdempotencyLockId() {}

    public IdempotencyLockId(String processId, String idempotencyKey) {
        this.processId = processId;
        this.idempotencyKey = idempotencyKey;
    }

    // Getters and Setters
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

    // Equals and HashCode based on the composite key
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IdempotencyLockId that = (IdempotencyLockId) o;

        if (!processId.equals(that.processId)) return false;
        return idempotencyKey.equals(that.idempotencyKey);
    }

    @Override
    public int hashCode() {
        int result = processId.hashCode();
        result = 31 * result + idempotencyKey.hashCode();
        return result;
    }
}

