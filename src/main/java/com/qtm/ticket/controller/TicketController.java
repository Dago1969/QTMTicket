package com.qtm.ticket.controller;

import com.qtm.ticket.dto.TicketDto;
import com.qtm.ticket.service.TicketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller per la gestione dei ticket.
 * Endpoint CRUD e ricerca.
 */
@Slf4j
@RestController
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    /**
     * POST /tickets - Crea un nuovo ticket
     */
    @PostMapping
    public ResponseEntity<TicketDto> createTicket(@RequestBody TicketDto ticketDto) {
        log.info("Richiesta creazione ticket");
        TicketDto created = ticketService.createTicket(ticketDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * GET /tickets/{id} - Recupera un ticket per ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<TicketDto> getTicket(@PathVariable Long id) {
        log.info("Richiesta recupero ticket ID: {}", id);
        TicketDto ticket = ticketService.getTicketById(id);
        return ResponseEntity.ok(ticket);
    }

    /**
     * PUT /tickets/{id} - Aggiorna un ticket
     */
    @PutMapping("/{id}")
    public ResponseEntity<TicketDto> updateTicket(@PathVariable Long id, @RequestBody TicketDto ticketDto) {
        log.info("Richiesta aggiornamento ticket ID: {}", id);
        TicketDto updated = ticketService.updateTicket(id, ticketDto);
        return ResponseEntity.ok(updated);
    }

    /**
     * DELETE /tickets/{id} - Elimina un ticket
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
        log.info("Richiesta eliminazione ticket ID: {}", id);
        ticketService.deleteTicket(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * GET /tickets/by-realm/{realm} - Recupera ticket per realm (paginato)
     */
    @GetMapping("/by-realm/{realm}")
    public ResponseEntity<Page<TicketDto>> getByRealm(@PathVariable String realm, Pageable pageable) {
        log.info("Richiesta ticket per realm: {}", realm);
        Page<TicketDto> tickets = ticketService.getTicketsByRealm(realm, pageable);
        return ResponseEntity.ok(tickets);
    }

    /**
     * GET /tickets/by-project/{project} - Recupera ticket per progetto (paginato)
     */
    @GetMapping("/by-project/{project}")
    public ResponseEntity<Page<TicketDto>> getByProject(@PathVariable String project, Pageable pageable) {
        log.info("Richiesta ticket per progetto: {}", project);
        Page<TicketDto> tickets = ticketService.getTicketsByProject(project, pageable);
        return ResponseEntity.ok(tickets);
    }

    /**
     * GET /tickets/by-patient/{patientId} - Recupera ticket per paziente (paginato)
     */
    @GetMapping("/by-patient/{patientId}")
    public ResponseEntity<Page<TicketDto>> getByPatient(@PathVariable String patientId, Pageable pageable) {
        log.info("Richiesta ticket per paziente: {}", patientId);
        Page<TicketDto> tickets = ticketService.getTicketsByPatient(patientId, pageable);
        return ResponseEntity.ok(tickets);
    }

    /**
     * GET /tickets/by-plan/{therapeuticPlanId} - Recupera ticket per piano terapeutico (paginato)
     */
    @GetMapping("/by-plan/{therapeuticPlanId}")
    public ResponseEntity<Page<TicketDto>> getByTherapeuticPlan(@PathVariable String therapeuticPlanId, Pageable pageable) {
        log.info("Richiesta ticket per piano terapeutico: {}", therapeuticPlanId);
        Page<TicketDto> tickets = ticketService.getTicketsByTherapeuticPlan(therapeuticPlanId, pageable);
        return ResponseEntity.ok(tickets);
    }

    /**
     * GET /tickets/by-status/{status} - Recupera ticket per stato (paginato)
     */
    @GetMapping("/by-status/{status}")
    public ResponseEntity<Page<TicketDto>> getByStatus(@PathVariable String status, Pageable pageable) {
        log.info("Richiesta ticket per stato: {}", status);
        Page<TicketDto> tickets = ticketService.getTicketsByStatus(status, pageable);
        return ResponseEntity.ok(tickets);
    }

    /**
     * GET /tickets/search - Ricerca con filtri multipli
     * Query params: realm, project, patientId, status
     */
    @GetMapping("/search")
    public ResponseEntity<Page<TicketDto>> searchTickets(
            @RequestParam(required = false) String realm,
            @RequestParam(required = false) String project,
            @RequestParam(required = false) String patientId,
            @RequestParam(required = false) String status,
            Pageable pageable) {
        log.info("Ricerca ticket con filtri: realm={}, project={}, patientId={}, status={}", 
            realm, project, patientId, status);
        Page<TicketDto> tickets = ticketService.searchTickets(realm, project, patientId, status, pageable);
        return ResponseEntity.ok(tickets);
    }

    /**
     * GET /tickets/patient/{patientId}/open - Recupera i ticket aperti per paziente
     */
    @GetMapping("/patient/{patientId}/open")
    public ResponseEntity<List<TicketDto>> getOpenByPatient(@PathVariable String patientId) {
        log.info("Richiesta ticket aperti per paziente: {}", patientId);
        List<TicketDto> tickets = ticketService.getOpenTicketsByPatient(patientId);
        return ResponseEntity.ok(tickets);
    }

    /**
     * PATCH /tickets/{id}/status/{newStatus} - Cambia lo stato di un ticket
     */
    @PatchMapping("/{id}/status/{newStatus}")
    public ResponseEntity<TicketDto> changeStatus(@PathVariable Long id, @PathVariable String newStatus) {
        log.info("Richiesta cambio stato ticket ID {} a: {}", id, newStatus);
        TicketDto updated = ticketService.changeStatus(id, newStatus);
        return ResponseEntity.ok(updated);
    }
}
