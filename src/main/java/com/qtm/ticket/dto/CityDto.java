package com.qtm.ticket.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO per la trasmissione dei dati di una città.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CityDto {
    private Long id;
    private Long provinceId;
    private String code;
    private String istatCode;
    private String catastaleCode;
    private String name;
    private Boolean capoluogo;
}
