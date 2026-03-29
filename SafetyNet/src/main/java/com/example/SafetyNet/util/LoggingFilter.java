package com.example.SafetyNet.util;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LoggingFilter implements Filter {

    // Cette ligne initialise le logger pour cette classe précise
    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        logger.info("Requête Entrante : {} {}", req.getMethod(), req.getRequestURI());

        chain.doFilter(request, response);

        logger.info("Réponse Sortante : {} | Statut : {}", req.getRequestURI(), res.getStatus());
    }
}