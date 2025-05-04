package com.example.idempotency;

public class IdempotencyContextHolder {

    private static final ThreadLocal<IdempotencyContext> idempotencyContexts = new ThreadLocal<>();

    public static IdempotencyContext get() {
        IdempotencyContext context = idempotencyContexts.get();
        if (context == null) {
            throw new IllegalStateException("There is no idempotency context. Probably this was executed outside of idempotency process");
        }
        return context;
    }

    static void set(IdempotencyContext idempotencyContext) {
        idempotencyContexts.set(idempotencyContext);
    }

    static void remove() {
        idempotencyContexts.remove();
    }
}

