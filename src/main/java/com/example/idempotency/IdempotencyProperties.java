package com.example.idempotency;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "banklitex.idempotency")
public class IdempotencyProperties {

    private static final long STORE_EVICTION_TIME_DAYS = 10L;
    private static final long LOCK_OBTAINING_TIMEOUT_SECONDS = 3L;
    private static final long LOCK_OBTAINING_INITIAL_INTERVAL_MILLIS = 100L;
    private static final double LOCK_OBTAINING_BACKOFF_MULTIPLIER = 2.0;

    private long inMemoryMaxSize = 10_000;
    private Duration storeEvictionTime = Duration.ofDays(STORE_EVICTION_TIME_DAYS);
    private Duration lockObtainingTimeout = Duration.ofSeconds(LOCK_OBTAINING_TIMEOUT_SECONDS);
    private Duration lockObtainingInitialInterval = Duration.ofMillis(LOCK_OBTAINING_INITIAL_INTERVAL_MILLIS);
    private double lockObtainingBackOffMultiplier = LOCK_OBTAINING_BACKOFF_MULTIPLIER;

    // Getters and setters
    public long getInMemoryMaxSize() {
        return inMemoryMaxSize;
    }

    public void setInMemoryMaxSize(long inMemoryMaxSize) {
        this.inMemoryMaxSize = inMemoryMaxSize;
    }

    public Duration getStoreEvictionTime() {
        return storeEvictionTime;
    }

    public void setStoreEvictionTime(Duration storeEvictionTime) {
        this.storeEvictionTime = storeEvictionTime;
    }

    public Duration getLockObtainingTimeout() {
        return lockObtainingTimeout;
    }

    public void setLockObtainingTimeout(Duration lockObtainingTimeout) {
        this.lockObtainingTimeout = lockObtainingTimeout;
    }

    public Duration getLockObtainingInitialInterval() {
        return lockObtainingInitialInterval;
    }

    public void setLockObtainingInitialInterval(Duration lockObtainingInitialInterval) {
        this.lockObtainingInitialInterval = lockObtainingInitialInterval;
    }

    public double getLockObtainingBackOffMultiplier() {
        return lockObtainingBackOffMultiplier;
    }

    public void setLockObtainingBackOffMultiplier(double lockObtainingBackOffMultiplier) {
        this.lockObtainingBackOffMultiplier = lockObtainingBackOffMultiplier;
    }
}

