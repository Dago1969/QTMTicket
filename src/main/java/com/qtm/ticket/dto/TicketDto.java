package com.qtm.ticket.dto;

import com.qtm.ticket.entity.TicketEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO per comunicazione esterna del ticket.
 * Converte Entity a DTO e viceversa per API REST.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class TicketDto {

    private Long id;
    private String realm;
    private String project;
    private String patientId;
    private String therapeuticPlanId;
    private String ticketType; // String instead of enum for API compatibility
    private String status; // String instead of enum for API compatibility
    private String title;
    private String description;
    private String contentJson;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
