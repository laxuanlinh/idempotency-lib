package com.example.idempotency;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.UUID;
import java.util.function.Function;

@Component
@EnableConfigurationProperties(IdempotencyProperties.class)
public class IdempotencyContextProvider {

    private static final Logger log = LoggerFactory.getLogger(IdempotencyContextProvider.class);
    private IdempotencyStore idempotencyStore;

    public IdempotencyContextProvider(IdempotencyStore idempotencyStore) {
        this.idempotencyStore = idempotencyStore;
    }

    public <R> R runIdempotentProcess(String processId, String idempotencyKey, Duration lockTimeout, Function<IdempotencyContext, R> body) {
        log.trace("Received idempotent process request for processId={}, idempotencyKey={}, lockTimeout={}",
                processId, idempotencyKey, lockTimeout);

        var context = new IdempotencyContext(processId, idempotencyKey, idempotencyStore);

        try (context) {
            IdempotencyContextHolder.set(context);

            UUID lockId = UUID.randomUUID();
            if (lockTimeout != null) {
                log.trace("Locking idempotenceStore for processId={}, idempotencyKey={}", processId, idempotencyKey);
                idempotencyStore.lockBlocking(processId, idempotencyKey, lockId, lockTimeout);
                context.holdingLockId = lockId;
            }

            return body.apply(IdempotencyContextHolder.get());
        }
    }
}

