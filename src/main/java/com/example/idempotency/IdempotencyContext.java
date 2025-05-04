package com.example.idempotency;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;

public class IdempotencyContext implements AutoCloseable {

    private static final Logger log = LoggerFactory.getLogger(IdempotencyContext.class);

    private final String processId;
    private final String idempotencyKey;
    private final IdempotencyStore idempotencyStore;
    private final ObjectMapper objectMapper;
    private final Set<String> operationIds = new HashSet<>();
    protected UUID holdingLockId;

    public IdempotencyContext(String processId, String idempotencyKey, IdempotencyStore idempotencyStore) {
        this.processId = processId;
        this.idempotencyKey = idempotencyKey;
        this.idempotencyStore = idempotencyStore;
        this.objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule());
    }

    
    public <R> R executeBodyOnce(String operationId, boolean transactional, Supplier<R> body, Class<R> responseClass) {
        log.trace("Received execution request with operationId={}", operationId);

        if (!operationIds.add(operationId)) {
            throw new RuntimeException("OperationAlreadyExecuted");
        }

        String storedValueOrNull = idempotencyStore.getStoredValueOrNull(processId, operationId, idempotencyKey);
        if (storedValueOrNull == null) {
            log.trace("No cached value found for processId={}, operationId={}, idempotencyKey={}, executing provided body",
                    processId, operationId, idempotencyKey);
            try {
                return idempotencyStore.saveResult(transactional, storeOperationResult -> {
                    R res = body.get();
                    try {
                        String serialized = objectMapper.writeValueAsString(res);
                        storeOperationResult.storeOperationResult(processId, idempotencyKey, operationId, serialized);
                        return res;
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to serialize result", e);
                    }
                });
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                storedValueOrNull = idempotencyStore.getStoredValueOrNull(processId, operationId, idempotencyKey);
            }
        }

        try {
            return objectMapper.readValue(storedValueOrNull, responseClass);
        } catch (IOException e) {
            throw new RuntimeException("Failed to deserialize stored value", e);
        }
    }


    @Override
    public void close() {
        log.trace("Unlocking idempotenceStore for processId={}, idempotencyKey={}", processId, idempotencyKey);
        if (holdingLockId != null) {
            idempotencyStore.releaseLock(processId, idempotencyKey, holdingLockId);
        }
        IdempotencyContextHolder.remove();
    }
}

