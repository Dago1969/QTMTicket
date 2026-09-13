package com.qtm.ticket.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO che espone la relazione struttura-disciplin a livello REST.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StructureDepartmentDto {

    private Long id;
    private String codiceStruttura;
    private String codiceDisciplina;
    private String disciplina;
    private String indirizzo;
}
