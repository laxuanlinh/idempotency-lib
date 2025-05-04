package com.example.idempotency.repository;

import com.example.idempotency.model.IdempotencyLock;
import com.example.idempotency.model.IdempotencyLockPK;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public interface IdempotencyLockRepository extends JpaRepository<IdempotencyLock, IdempotencyLockPK> {

    @Transactional
    @Modifying
    @Query(value = """
            INSERT INTO idempotency_lock(process_id, idempotency_key, lock_Id, locked_until)
            VALUES (:process_id, :idempotency_key, :lock_Id, :until)
            ON CONFLICT ON CONSTRAINT idempotency_lock_pk
            DO UPDATE SET locked_until = :until, lock_id = :lock_Id
            WHERE idempotency_lock.locked_until < :now
            """, nativeQuery = true)
    int obtainLock(
            @Param("process_id") String processId,
            @Param("idempotency_key") String idempotencyKey,
            @Param("lock_Id") String lockId,
            @Param("now") Instant now,
            @Param("until") Instant until
    );

    @Transactional
    @Modifying
    @Query(value = """
            DELETE FROM idempotency_lock
            WHERE process_id = :process_id
              AND idempotency_key = :idempotency_key
              AND lock_Id = :lock_Id
            """, nativeQuery = true)
    int releaseLock(
            @Param("process_id") String processId,
            @Param("idempotency_key") String idempotencyKey,
            @Param("lock_Id") String lockId
    );

    @Transactional
    @Modifying
    @Query("DELETE FROM IdempotencyLock l WHERE l.lockedUntil < :before")
    int deleteWhereLockedUntilBefore(@Param("before") Instant before);
}

