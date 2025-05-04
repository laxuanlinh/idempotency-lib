package com.example.idempotency;

import com.example.idempotency.IdempotencyProperties;
import com.example.idempotency.IdempotencyStore;
import com.example.idempotency.model.IdempotencyRecord;
import com.example.idempotency.repository.IdempotencyLockRepository;
import com.example.idempotency.repository.IdempotencyRecordRepository;
import com.example.idempotency.service.InTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;

@Service
public class IdempotencyStore {

    private IdempotencyRecordRepository idempotencyRecordRepository;

    private IdempotencyLockRepository idempotencyLockRepository;

    private InTransactionService inTransactionService;

    private IdempotencyProperties idempotencyProperties;

    public IdempotencyStore(IdempotencyRecordRepository idempotencyRecordRepository, IdempotencyLockRepository idempotencyLockRepository, InTransactionService inTransactionService, IdempotencyProperties idempotencyProperties, PostgresStoreOperationResult postgresStoreOperationResult) {
        this.idempotencyRecordRepository = idempotencyRecordRepository;
        this.idempotencyLockRepository = idempotencyLockRepository;
        this.inTransactionService = inTransactionService;
        this.idempotencyProperties = idempotencyProperties;
        this.postgresStoreOperationResult = postgresStoreOperationResult;
    }

    private PostgresStoreOperationResult postgresStoreOperationResult;
    
    public String getStoredValueOrNull(String processId, String operationId, String idempotencyKey) {
        return idempotencyRecordRepository.findById(
                processId + "-" + idempotencyKey + "-" + operationId
        ).map(IdempotencyRecord::getData).orElse(null);
    }
    
    public <R> R saveResult(boolean transactional, Function<PostgresStoreOperationResult, R> body) {
        try {
            if (transactional) {
                return inTransactionService.executeInNewTransaction(postgresStoreOperationResult, body);
            } else {
                return body.apply(postgresStoreOperationResult);
            }
        } catch (DataIntegrityViolationException ex) {
            if (ex.getMessage() != null && ex.getMessage().contains("idempotency_record_pk")) {
                throw new RuntimeException();
            } else {
                throw ex;
            }
        }
    }

    public void lockBlocking(String processId, String idempotencyKey, UUID lockId, Duration timeout) {
        RetryTemplate retryTemplate = RetryTemplate.builder()
                .withTimeout(idempotencyProperties.getLockObtainingTimeout())
                .exponentialBackoff(
                        idempotencyProperties.getLockObtainingInitialInterval().toMillis(),
                        idempotencyProperties.getLockObtainingBackOffMultiplier(),
                        idempotencyProperties.getLockObtainingTimeout().toMillis())
                .retryOn(RuntimeException.class)
                .build();

        try {
            retryTemplate.execute(context -> {
                int updated = idempotencyLockRepository.obtainLock(
                        processId,
                        idempotencyKey,
                        lockId.toString(),
                        Instant.now(),
                        Instant.now().plus(timeout)
                );
                if (updated == 0) {
                    throw new RuntimeException();
                }
                return null;
            });
        } catch (RuntimeException e) {
            throw new RuntimeException("Unable to obtain process level lock in the expected time");
        }
    }
    
    public void releaseLock(String processId, String idempotencyKey, UUID lockId) {
        idempotencyLockRepository.releaseLock(processId, idempotencyKey, lockId.toString());
    }


} 