package com.qtm.ticket.exception;

/**
 * Eccezione lanciata per errori di validazione del ticket.
 */
public class TicketValidationException extends RuntimeException {
    public TicketValidationException(String message) {
        super(message);
    }

    public TicketValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
