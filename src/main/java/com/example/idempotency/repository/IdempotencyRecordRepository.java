package com.example.idempotency.repository;

import com.example.idempotency.model.IdempotencyRecord;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public interface IdempotencyRecordRepository extends JpaRepository<IdempotencyRecord, String> {

    @Transactional
    @Modifying
    @Query(value = """
        INSERT INTO idempotency_record (id, created_at, data)
        VALUES (:id, :created_at, :data)
        """, nativeQuery = true)
    void insert(
            @Param("id") String id,
            @Param("created_at") Instant createdAt,
            @Param("data") String data
    );

    @Transactional
    @Modifying
    @Query(value = """
        DELETE FROM idempotency_record
        WHERE id IN (
            SELECT id FROM idempotency_record
            WHERE created_at < :before
            LIMIT :limit
        )
        """, nativeQuery = true)
    int deleteWhereCreatedAtBefore(
            @Param("before") Instant before,
            @Param("limit") int limit
    );
}

