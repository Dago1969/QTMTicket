package com.qtm.ticket.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO per la trasmissione dei dati di una regione.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class RegionDto {
    private Long id;
    private String code;
    private String name;
    private String geographicArea;
    private String regionType;
}
