package com.qtm.ticket.service;

import com.qtm.ticket.dto.TicketDto;
import com.qtm.ticket.entity.TicketEntity;
import com.qtm.ticket.exception.TicketNotFoundException;
import com.qtm.ticket.exception.TicketValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test di integrazione per il TicketService.
 * Testa CRUD operations e ricerca.
 */
@SpringBootTest
@ActiveProfiles("test")
public class TicketServiceIntegrationTest {

    @Autowired
    private TicketService ticketService;

    private TicketDto testTicket;

    @BeforeEach
    public void setUp() {
        testTicket = TicketDto.builder()
                .realm("TEST_REALM")
                .project("TEST_PROJECT")
                .patientId("PAT-001")
                .therapeuticPlanId("PLAN-001")
                .ticketType("BUG")
                .status("OPEN")
                .title("Test Ticket")
                .description("Test Description")
                .contentJson("{\"key\": \"value\"}")
                .build();
    }

    @Test
    public void testCreateTicket() {
        // When
        TicketDto created = ticketService.createTicket(testTicket);

        // Then
        assertNotNull(created);
        assertNotNull(created.getId());
        assertEquals("TEST_REALM", created.getRealm());
        assertEquals("PAT-001", created.getPatientId());
        assertEquals("OPEN", created.getStatus());
    }

    @Test
    public void testGetTicketById() {
        // Given
        TicketDto created = ticketService.createTicket(testTicket);

        // When
        TicketDto retrieved = ticketService.getTicketById(created.getId());

        // Then
        assertNotNull(retrieved);
        assertEquals(created.getId(), retrieved.getId());
        assertEquals("Test Ticket", retrieved.getTitle());
    }

    @Test
    public void testGetTicketById_NotFound() {
        // When & Then
        assertThrows(TicketNotFoundException.class, () -> ticketService.getTicketById(999L));
    }

    @Test
    public void testUpdateTicket() {
        // Given
        TicketDto created = ticketService.createTicket(testTicket);
        TicketDto updated = created.toBuilder()
                .title("Updated Title")
                .status("IN_PROGRESS")
                .build();

        // When
        TicketDto result = ticketService.updateTicket(created.getId(), updated);

        // Then
        assertNotNull(result);
        assertEquals("Updated Title", result.getTitle());
        assertEquals("IN_PROGRESS", result.getStatus());
    }

    @Test
    public void testDeleteTicket() {
        // Given
        TicketDto created = ticketService.createTicket(testTicket);

        // When
        ticketService.deleteTicket(created.getId());

        // Then
        assertThrows(TicketNotFoundException.class, () -> ticketService.getTicketById(created.getId()));
    }

    @Test
    public void testGetTicketsByRealm() {
        // Given
        ticketService.createTicket(testTicket);
        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<TicketDto> result = ticketService.getTicketsByRealm("TEST_REALM", pageable);

        // Then
        assertNotNull(result);
        assertTrue(result.getContent().size() > 0);
    }

    @Test
    public void testGetTicketsByPatient() {
        // Given
        ticketService.createTicket(testTicket);
        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<TicketDto> result = ticketService.getTicketsByPatient("PAT-001", pageable);

        // Then
        assertNotNull(result);
        assertTrue(result.getContent().size() > 0);
    }

    @Test
    public void testChangeStatus() {
        // Given
        TicketDto created = ticketService.createTicket(testTicket);

        // When
        TicketDto updated = ticketService.changeStatus(created.getId(), "CLOSED");

        // Then
        assertEquals("CLOSED", updated.getStatus());
    }

    @Test
    public void testValidationException() {
        // Given
        TicketDto invalid = TicketDto.builder()
                .realm(null)
                .project("TEST")
                .patientId("PAT-001")
                .ticketType("BUG")
                .title("Test")
                .build();

        // When & Then
        assertThrows(TicketValidationException.class, () -> ticketService.createTicket(invalid));
    }

    @Test
    public void testGetOpenTicketsByPatient() {
        // Given
        ticketService.createTicket(testTicket);
        
        TicketDto closedTicket = testTicket.toBuilder()
                .title("Closed Ticket")
                .status("CLOSED")
                .build();
        ticketService.createTicket(closedTicket);

        // When
        List<TicketDto> openTickets = ticketService.getOpenTicketsByPatient("PAT-001");

        // Then
        assertNotNull(openTickets);
        assertTrue(openTickets.stream().allMatch(t -> !t.getStatus().equals("CLOSED")));
    }
}
