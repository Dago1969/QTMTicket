package com.qtm.ticket.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO per la trasmissione delle strutture sanitarie.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class StructureDto {
    private Long id;
    private String codiceRegione;
    private String codiceAzienda;
    private String codiceStruttura;
    private String denominazioneStruttura;
    private String indirizzo;
    private String codiceComune;
    private String comune;
    private String siglaProvincia;
    private Long cityId;
    private String structureTypeCode;
    private String structureTypeDescription;
}
