package com.qtm.ticket.service;

/**
 * Filtri disponibili per la ricerca completa del catalogo farmaci.
 */
public record MedicineSearchCriteria(
        String codiceAic,
        String codFarmaco,
        String codConfezione,
        String denominazione,
        String descrizione,
        String codiceAtc,
        String ragioneSociale,
        String statoAmministrativo
) {
}