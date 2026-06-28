package com.qtm.ticket.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO per la trasmissione dei dati di una provincia.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ProvinceDto {
    private Long id;
    private Long regionId;
    private String code;
    private String name;
    private String sigla;
}
