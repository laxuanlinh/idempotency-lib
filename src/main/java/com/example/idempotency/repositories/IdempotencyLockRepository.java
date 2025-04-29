package com.example.idempotency.repositories;

import com.example.idempotency.models.IdempotencyLock;
import com.example.idempotency.models.IdempotencyLockId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IdempotencyLockRepository extends JpaRepository<IdempotencyLock, IdempotencyLockId> {
    boolean existsById(String idempotencyKey);
}
