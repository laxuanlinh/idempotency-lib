package com.example.idempotency.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "idempotency_lock")
@IdClass(IdempotencyLockId.class)
public class IdempotencyLock {

    @Id
    @Column(name = "process_id", length = 255)
    private String processId;

    @Id
    @Column(name = "idempotency_key", length = 255)
    private String idempotencyKey;

    @Column(name = "lock_id", length = 36, nullable = false)
    private String lockId;

    @Column(name = "locked_until", nullable = false)
    private LocalDateTime lockedUntil;

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

    public String getLockId() {
        return lockId;
    }

    public void setLockId(String lockId) {
        this.lockId = lockId;
    }

    public LocalDateTime getLockedUntil() {
        return lockedUntil;
    }

    public void setLockedUntil(LocalDateTime lockedUntil) {
        this.lockedUntil = lockedUntil;
    }
}

