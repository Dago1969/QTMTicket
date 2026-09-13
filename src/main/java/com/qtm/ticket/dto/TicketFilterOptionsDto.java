package com.qtm.ticket.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO che espone i valori distinti disponibili nel DB ticket per popolare i filtri della UI.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketFilterOptionsDto {

    private List<String> realms;
    private List<String> projects;
    private List<String> patientIds;
    private List<String> statuses;
}