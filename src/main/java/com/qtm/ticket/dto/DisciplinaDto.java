package com.qtm.ticket.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO per le discipline sanitarie esposte via REST.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DisciplinaDto {

    private String codiceDisciplina;
    private String disciplina;
}
