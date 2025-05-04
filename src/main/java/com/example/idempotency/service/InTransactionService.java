package com.example.idempotency.service;

import com.example.idempotency.IdempotencyStore;
import com.example.idempotency.PostgresStoreOperationResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.util.function.Function;

@Service
public class InTransactionService {

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public <R> R executeInNewTransaction(PostgresStoreOperationResult storeOperationResult, Function<PostgresStoreOperationResult, R> body) {
        return body.apply(storeOperationResult);
    }
}

