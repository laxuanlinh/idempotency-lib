package com.example.idempotency.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "idempotency_lock")
public class IdempotencyLock {

    @EmbeddedId
    private IdempotencyLockPK key;

    @Column(name = "lock_id", nullable = false)
    private String lockId;

    @Column(name = "locked_until", nullable = false)
    private Instant lockedUntil;

    // Constructors
    public IdempotencyLock() {
    }

    public IdempotencyLock(IdempotencyLockPK key, String lockId, Instant lockedUntil) {
        this.key = key;
        this.lockId = lockId;
        this.lockedUntil = lockedUntil;
    }

    // Getters and setters
    public IdempotencyLockPK getKey() {
        return key;
    }

    public void setKey(IdempotencyLockPK key) {
        this.key = key;
    }

    public String getLockId() {
        return lockId;
    }

    public void setLockId(String lockId) {
        this.lockId = lockId;
    }

    public Instant getLockedUntil() {
        return lockedUntil;
    }

    public void setLockedUntil(Instant lockedUntil) {
        this.lockedUntil = lockedUntil;
    }
}

