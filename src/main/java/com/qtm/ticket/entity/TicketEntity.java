package com.qtm.ticket.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entity per la gestione dei ticket centralizzati.
 * Riferimenti a realm, progetto, paziente, piano terapeutico.
 * Contiene JSON per la gestione del contenuto dinamico.
 */
@Entity
@Table(name = "tickets")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Realm di provenienza del ticket (es. "REALM1", "REALM2", ecc.)
     */
    @Column(name = "realm", nullable = false, length = 100)
    private String realm;

    /**
     * Progetto di provenienza (es. "TENANTS", "DASHBOARD", ecc.)
     */
    @Column(name = "project", nullable = false, length = 100)
    private String project;

    /**
     * ID del paziente associato
     */
    @Column(name = "patient_id", nullable = false, length = 100)
    private String patientId;

    /**
     * ID del piano terapeutico associato
     */
    @Column(name = "therapeutic_plan_id", length = 100)
    private String therapeuticPlanId;

    /**
     * Tipologia del ticket (es. "BUG", "FEATURE", "SUPPORT", "ISSUE", ecc.)
     */
    @Column(name = "ticket_type", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private TicketType ticketType;

    /**
     * Stato del ticket (es. "OPEN", "IN_PROGRESS", "CLOSED", ecc.)
     */
    @Column(name = "status", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private TicketStatus status;

    /**
     * Titolo/Oggetto del ticket
     */
    @Column(name = "title", nullable = false, length = 500)
    private String title;

    /**
     * Descrizione del ticket
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    /**
     * Contenuto in JSON per gestione dinamica del ticket.
     * Può contenere dati aggiuntivi, metadati, ecc.
     */
    @Column(name = "content_json", columnDefinition = "TEXT")
    private String contentJson;

    /**
     * Data/ora di creazione
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Data/ora di ultimo aggiornamento
     */
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Callback per impostare le date automaticamente
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    /**
     * Enum per le tipologie di ticket
     */
    public enum TicketType {
        BUG,
        FEATURE,
        SUPPORT,
        ISSUE,
        ENHANCEMENT,
        DOCUMENTATION
    }

    /**
     * Enum per lo stato del ticket
     */
    public enum TicketStatus {
        OPEN,
        IN_PROGRESS,
        ON_HOLD,
        CLOSED,
        REJECTED,
        REOPENED
    }
}
