package com.qtm.ticket.service;

import lombok.Data;

@Data
public class AslCsvRecordDto {
    private String codiceAzienda;
    private String denominazioneAzienda;
    private String codiceRegione;
    private String denominazioneRegione;
    private String comune;
    private String siglaProvincia;
    private String indirizzo;
    private String cap;
    private String telefono;
    private String fax;
    private String email;
    private String sitoWeb;
    private String partitaIva;
    private Integer anno;
}
