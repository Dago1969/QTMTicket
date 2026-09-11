package com.qtm.ticket.service;

import lombok.Data;

/**
 * Record intermedio usato per importare il dataset ospedali dal CSV ministeriale.
 */
@Data
public class HospitalCsvRecordDto {
    private Integer anno;
    private String codiceRegione;
    private String regione;
    private String codiceAsl;
    private String asl;
    private String codiceStruttura;
    private String denominazioneStruttura;
    private String comune;
    private String siglaProvincia;
    private String codiceTipoStruttura;
    private String tipoStruttura;
    private String indirizzo;
}