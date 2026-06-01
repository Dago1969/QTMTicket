package com.qtm.ticket.controller;

import com.qtm.ticket.exception.TicketNotFoundException;
import com.qtm.ticket.exception.TicketValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Global Exception Handler per la gestione centralizzata delle eccezioni.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Gestisce TicketNotFoundException
     */
    @ExceptionHandler(TicketNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleTicketNotFound(TicketNotFoundException e) {
        log.error("Ticket non trovato: {}", e.getMessage());
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.NOT_FOUND.value());
        response.put("message", e.getMessage());
        response.put("detail", "Il ticket richiesto non esiste");
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    /**
     * Gestisce TicketValidationException
     */
    @ExceptionHandler(TicketValidationException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(TicketValidationException e) {
        log.error("Errore di validazione: {}", e.getMessage());
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("message", "Errore di validazione");
        response.put("detail", e.getMessage());
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Gestisce eccezioni generiche
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception e) {
        log.error("Errore interno del server", e);
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.put("message", "Errore interno del server");
        response.put("detail", e.getMessage());
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
