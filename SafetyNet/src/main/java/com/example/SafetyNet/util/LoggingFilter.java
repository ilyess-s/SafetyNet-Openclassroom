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

    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // Log de la requête (INFO)
        logger.info("Requête Entrante : {} {}", req.getMethod(), req.getRequestURI());

        chain.doFilter(request, response);

        // Analyse du statut de la réponse
        int status = res.getStatus();

        if (status >= 500) {
            // Niveau ERROR pour les 500 (ce que vous avez actuellement)
            logger.error("Réponse Sortante : {} | Statut : {} | ERREUR INTERNE", req.getRequestURI(), status);
        } else if (status >= 400) {
            // Niveau WARN pour les 400 et 404
            logger.warn("Réponse Sortante : {} | Statut : {} | RESSOURCE NON TROUVÉE", req.getRequestURI(), status);
        } else {
            // Niveau INFO pour les succès (200, 201)
            logger.info("Réponse Sortante : {} | Statut : {} | SUCCÈS", req.getRequestURI(), status);
        }
    }
}
