package com.qtm.ticket.exception;

/**
 * Eccezione lanciata quando un ticket non viene trovato.
 */
public class TicketNotFoundException extends RuntimeException {
    public TicketNotFoundException(Long id) {
        super("Ticket con ID " + id + " non trovato");
    }

    public TicketNotFoundException(String message) {
        super(message);
    }
}
