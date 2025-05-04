package com.example.idempotency;


import com.example.idempotency.repository.IdempotencyRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class PostgresStoreOperationResult{

    @Autowired
    private IdempotencyRecordRepository idempotencyRecordRepository;
    public void storeOperationResult(String processId, String idempotencyKey, String operationId, String result) {
        idempotencyRecordRepository.insert(
                idempotencyRecordId(processId, idempotencyKey, operationId),
                Instant.now(),
                result
        );
    }
    private String idempotencyRecordId(String processId, String idempotencyKey, String operationId) {
        return processId + "-" + idempotencyKey + "-" + operationId;
    }
}
