package com.example.idempotency;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;

@Service
public interface IdempotencyService {

    String checkAndLock(HttpServletRequest servletRequest);

}
