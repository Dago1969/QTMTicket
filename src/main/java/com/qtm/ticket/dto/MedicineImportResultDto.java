package com.qtm.ticket.dto;

/**
 * Riepilogo delle righe importate dal catalogo AIFA.
 */
public record MedicineImportResultDto(long importedRows, long skippedRows) {
}