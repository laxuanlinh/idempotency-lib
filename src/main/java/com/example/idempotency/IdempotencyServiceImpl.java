package com.example.idempotency;

import com.example.idempotency.models.IdempotencyLock;
import com.example.idempotency.models.IdempotencyRecord;
import com.example.idempotency.repositories.IdempotencyLockRepository;
import com.example.idempotency.repositories.IdempotencyRecordRepository;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Enumeration;

@Service
public class IdempotencyServiceImpl implements IdempotencyService{
    @Autowired
    private IdempotencyRecordRepository idempotencyRecordRepository;

    @Autowired
    private IdempotencyLockRepository idempotencyLockRepository;

    @Transactional
    public String checkAndLock(HttpServletRequest request) {

        if ("GET".equalsIgnoreCase(request.getMethod())) {
            return null;
        }

        // Generate idempotency key by hashing the endpoint, params, and body
        String idempotencyKey = generateIdempotencyKey(request);

        // Check if the idempotency key exists in the lock table
        if (idempotencyLockRepository.existsById(idempotencyKey)) {
            // Return cached response data if already processed
            var record = idempotencyRecordRepository.findById(idempotencyKey).orElse(null);
            if (record != null) {
                return record.getData();  // Return the data from the record table
            }
        }

        // Create a lock entry to prevent others from processing the same request
        var lock = new IdempotencyLock();
        lock.setLockId(generateLockId());
        lock.setIdempotencyKey(idempotencyKey);
        lock.setProcessId(request.getRemoteAddr()); // or use any unique identifier for process
        lock.setLockedUntil(java.time.LocalDateTime.now().plusMinutes(10)); // Set expiration for lock

        idempotencyLockRepository.save(lock);
    }

    // Method to generate a unique idempotency key based on the endpoint, parameters, and body
    private String generateIdempotencyKey(HttpServletRequest request) {
        StringBuilder keyBuilder = new StringBuilder();

        // Add method, URI, query params, and body to the key
        keyBuilder.append(request.getMethod()).append(request.getRequestURI());

        // Add query parameters
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            keyBuilder.append(paramName).append("=").append(request.getParameter(paramName));
        }

        // Add request body (for POST/PUT methods)
        if (request.getMethod().equals("POST") || request.getMethod().equals("PUT")) {
            String body = getRequestBody(request);
            keyBuilder.append(body);
        }

        // Hash the key to generate a unique idempotency key
        return hashString(keyBuilder.toString());
    }

    // Hash the generated string to create a unique key
    private String hashString(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    // Get the request body as a string (you can enhance this based on your specific use case)
    private String getRequestBody(HttpServletRequest request) {
        // Here, you can read the body of the request if needed
        // For simplicity, we just return an empty string, but you can customize this to read from request input stream
        return "";
    }

    // Generate a unique lock ID
    private String generateLockId() {
        return java.util.UUID.randomUUID().toString();
    }
}
