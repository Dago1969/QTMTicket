package com.qtm.ticket.repository;

import com.qtm.ticket.entity.TicketEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository per l'accesso ai dati dei Ticket.
 */
@Repository
public interface TicketRepository extends JpaRepository<TicketEntity, Long> {

    /**
     * Trova i ticket per realm
     */
    Page<TicketEntity> findByRealm(String realm, Pageable pageable);

    /**
     * Trova i ticket per progetto
     */
    Page<TicketEntity> findByProject(String project, Pageable pageable);

    /**
     * Trova i ticket per paziente
     */
    Page<TicketEntity> findByPatientId(String patientId, Pageable pageable);

    /**
     * Trova i ticket per piano terapeutico
     */
    Page<TicketEntity> findByTherapeuticPlanId(String therapeuticPlanId, Pageable pageable);

    /**
     * Trova i ticket per tipologia
     */
    Page<TicketEntity> findByTicketType(TicketEntity.TicketType ticketType, Pageable pageable);

    /**
     * Trova i ticket per stato
     */
    Page<TicketEntity> findByStatus(TicketEntity.TicketStatus status, Pageable pageable);

    /**
     * Ricerca con filtri multipli
     */
    @Query("SELECT t FROM TicketEntity t WHERE " +
           "(:realm IS NULL OR t.realm = :realm) AND " +
           "(:project IS NULL OR t.project = :project) AND " +
           "(:patientId IS NULL OR t.patientId = :patientId) AND " +
           "(:status IS NULL OR t.status = :status)")
    Page<TicketEntity> findByFilters(
            @Param("realm") String realm,
            @Param("project") String project,
            @Param("patientId") String patientId,
            @Param("status") TicketEntity.TicketStatus status,
            Pageable pageable
    );

    /**
     * Conta i ticket per status e realm
     */
    long countByStatusAndRealm(TicketEntity.TicketStatus status, String realm);

    /**
     * Trova i ticket aperti per paziente
     */
    @Query("SELECT t FROM TicketEntity t WHERE t.patientId = :patientId AND t.status IN ('OPEN', 'IN_PROGRESS', 'ON_HOLD')")
    List<TicketEntity> findOpenTicketsByPatient(@Param("patientId") String patientId);
}
