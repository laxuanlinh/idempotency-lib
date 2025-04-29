package com.example.idempotency;


import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import java.io.IOException;

public class IdempotencyFilter implements Filter {

    private IdempotencyService idempotencyService;
    public IdempotencyFilter(IdempotencyService idempotencyService) {
        this.idempotencyService = idempotencyService;
    }


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String response = idempotencyService.checkAndLock((HttpServletRequest) servletRequest);
        if (response != null) {
            servletResponse.getWriter().write(response);
        }
    }
}
