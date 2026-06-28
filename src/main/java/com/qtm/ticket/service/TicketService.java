package com.qtm.ticket.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qtm.ticket.dto.TicketDto;
import com.qtm.ticket.entity.TicketEntity;
import com.qtm.ticket.exception.TicketNotFoundException;
import com.qtm.ticket.exception.TicketValidationException;
import com.qtm.ticket.mapper.TicketMapper;
import com.qtm.ticket.repository.TicketRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * Service per la gestione dei ticket.
 * Orchestrata repository e mapper, contiene logica di business.
 */
@Slf4j
@Service
@Transactional
public class TicketService {

    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;

    public TicketService(TicketRepository ticketRepository, TicketMapper ticketMapper) {
        this.ticketRepository = ticketRepository;
        this.ticketMapper = ticketMapper;
    }

    /**
     * Crea un nuovo ticket
     */
    public TicketDto createTicket(TicketDto ticketDto) {
        log.debug("Creazione nuovo ticket: realm={}, project={}, patientId={}", 
            ticketDto.getRealm(), ticketDto.getProject(), ticketDto.getPatientId());

        validateTicket(ticketDto);

        TicketEntity entity = ticketMapper.dtoToEntity(ticketDto);
        entity.setStatus(TicketEntity.TicketStatus.OPEN); // Default status
        
        TicketEntity saved = ticketRepository.save(entity);
        log.info("Ticket creato con ID: {}", saved.getId());
        
        return ticketMapper.entityToDto(saved);
    }

    /**
     * Recupera un ticket per ID
     */
    public TicketDto getTicketById(Long id) {
        log.debug("Recupero ticket con ID: {}", id);
        
        TicketEntity entity = ticketRepository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException(id));
        
        return ticketMapper.entityToDto(entity);
    }

    /**
     * Aggiorna un ticket esistente
     */
    public TicketDto updateTicket(Long id, TicketDto ticketDto) {
        log.debug("Aggiornamento ticket ID: {}", id);

        validateTicket(ticketDto);

        TicketEntity entity = ticketRepository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException(id));

        // Aggiorna i campi modificabili
        entity.setTitle(ticketDto.getTitle());
        entity.setDescription(ticketDto.getDescription());
        entity.setContentJson(ticketDto.getContentJson());
        if (ticketDto.getStatus() != null) {
            entity.setStatus(TicketEntity.TicketStatus.valueOf(ticketDto.getStatus()));
        }
        if (ticketDto.getTicketType() != null) {
            entity.setTicketType(TicketEntity.TicketType.valueOf(ticketDto.getTicketType()));
        }

        TicketEntity updated = ticketRepository.save(entity);
        log.info("Ticket ID {} aggiornato", id);
        
        return ticketMapper.entityToDto(updated);
    }

    /**
     * Elimina un ticket
     */
    public void deleteTicket(Long id) {
        log.debug("Eliminazione ticket ID: {}", id);

        if (!ticketRepository.existsById(id)) {
            throw new TicketNotFoundException(id);
        }

        ticketRepository.deleteById(id);
        log.info("Ticket ID {} eliminato", id);
    }

    /**
     * Recupera i ticket per realm (paginato)
     */
    public Page<TicketDto> getTicketsByRealm(String realm, Pageable pageable) {
        log.debug("Recupero ticket per realm: {}", realm);
        
        return ticketRepository.findByRealm(realm, pageable)
                .map(ticketMapper::entityToDto);
    }

    /**
     * Recupera i ticket per progetto (paginato)
     */
    public Page<TicketDto> getTicketsByProject(String project, Pageable pageable) {
        log.debug("Recupero ticket per progetto: {}", project);
        
        return ticketRepository.findByProject(project, pageable)
                .map(ticketMapper::entityToDto);
    }

    /**
     * Recupera i ticket per paziente (paginato)
     */
    public Page<TicketDto> getTicketsByPatient(String patientId, Pageable pageable) {
        log.debug("Recupero ticket per paziente: {}", patientId);
        
        return ticketRepository.findByPatientId(patientId, pageable)
                .map(ticketMapper::entityToDto);
    }

    /**
     * Recupera i ticket per piano terapeutico (paginato)
     */
    public Page<TicketDto> getTicketsByTherapeuticPlan(String therapeuticPlanId, Pageable pageable) {
        log.debug("Recupero ticket per piano terapeutico: {}", therapeuticPlanId);
        
        return ticketRepository.findByTherapeuticPlanId(therapeuticPlanId, pageable)
                .map(ticketMapper::entityToDto);
    }

    /**
     * Recupera i ticket per stato (paginato)
     */
    public Page<TicketDto> getTicketsByStatus(String status, Pageable pageable) {
        log.debug("Recupero ticket per stato: {}", status);
        
        TicketEntity.TicketStatus ticketStatus = TicketEntity.TicketStatus.valueOf(status);
        return ticketRepository.findByStatus(ticketStatus, pageable)
                .map(ticketMapper::entityToDto);
    }

    /**
     * Ricerca con filtri multipli
     */
    public Page<TicketDto> searchTickets(String realm, String project, String patientId, String status, Pageable pageable) {
        log.debug("Ricerca ticket con filtri: realm={}, project={}, patientId={}, status={}", 
            realm, project, patientId, status);

        TicketEntity.TicketStatus ticketStatus = status != null ? TicketEntity.TicketStatus.valueOf(status) : null;
        
        return ticketRepository.findByFilters(realm, project, patientId, ticketStatus, pageable)
                .map(ticketMapper::entityToDto);
    }

    /**
     * Recupera i ticket aperti per paziente
     */
    public List<TicketDto> getOpenTicketsByPatient(String patientId) {
        log.debug("Recupero ticket aperti per paziente: {}", patientId);
        
        return ticketRepository.findOpenTicketsByPatient(patientId)
                .stream()
                .map(ticketMapper::entityToDto)
                .collect(Collectors.toList());
    }

    /**
     * Cambia lo stato di un ticket
     */
    public TicketDto changeStatus(Long id, String newStatus) {
        log.debug("Cambio stato ticket ID {} a: {}", id, newStatus);

        TicketEntity entity = ticketRepository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException(id));

        try {
            entity.setStatus(TicketEntity.TicketStatus.valueOf(newStatus));
        } catch (IllegalArgumentException e) {
            throw new TicketValidationException("Stato non valido: " + newStatus);
        }

        TicketEntity updated = ticketRepository.save(entity);
        log.info("Stato ticket ID {} cambiato a {}", id, newStatus);
        
        return ticketMapper.entityToDto(updated);
    }

    /**
     * Valida i campi obbligatori del ticket
     */
    private void validateTicket(TicketDto ticketDto) {
        if (ticketDto.getRealm() == null || ticketDto.getRealm().isBlank()) {
            throw new TicketValidationException("Il realm è obbligatorio");
        }
        if (ticketDto.getProject() == null || ticketDto.getProject().isBlank()) {
            throw new TicketValidationException("Il project è obbligatorio");
        }
        if (ticketDto.getPatientId() == null || ticketDto.getPatientId().isBlank()) {
            throw new TicketValidationException("Il patientId è obbligatorio");
        }
        if (ticketDto.getTitle() == null || ticketDto.getTitle().isBlank()) {
            throw new TicketValidationException("Il title è obbligatorio");
        }
        if (ticketDto.getTicketType() == null || ticketDto.getTicketType().isBlank()) {
            throw new TicketValidationException("Il ticketType è obbligatorio");
        }
    }
}
