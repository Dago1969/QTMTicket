package com.qtm.ticket.mapper;

import org.springframework.stereotype.Component;

import com.qtm.ticket.dto.TicketDto;
import com.qtm.ticket.entity.TicketEntity;

import lombok.extern.slf4j.Slf4j;

/**
 * Mapper per conversione tra Entity e DTO per il Ticket.
 */
@Slf4j
@Component
public class TicketMapper {

    /**
     * Converte TicketEntity a TicketDto
     */
    public TicketDto entityToDto(TicketEntity entity) {
        if (entity == null) {
            return null;
        }
        return TicketDto.builder()
                .id(entity.getId())
                .realm(entity.getRealm())
                .project(entity.getProject())
                .patientId(entity.getPatientId())
                .therapeuticPlanId(entity.getTherapeuticPlanId())
                .ticketType(entity.getTicketType() != null ? entity.getTicketType().name() : null)
                .status(entity.getStatus() != null ? entity.getStatus().name() : null)
                .title(entity.getTitle())
                .description(entity.getDescription())
                .contentJson(entity.getContentJson())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    /**
     * Converte TicketDto a TicketEntity
     */
    public TicketEntity dtoToEntity(TicketDto dto) {
        if (dto == null) {
            return null;
        }
        return TicketEntity.builder()
                .id(dto.getId())
                .realm(dto.getRealm())
                .project(dto.getProject())
                .patientId(dto.getPatientId())
                .therapeuticPlanId(dto.getTherapeuticPlanId())
                .ticketType(dto.getTicketType() != null ? TicketEntity.TicketType.valueOf(dto.getTicketType()) : null)
                .status(dto.getStatus() != null ? TicketEntity.TicketStatus.valueOf(dto.getStatus()) : null)
                .title(dto.getTitle())
                .description(dto.getDescription())
                .contentJson(dto.getContentJson())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }
}
