package com.qtm.ticket.controller;

/**
 * Richiesta per abilitare o disabilitare la visibilita del farmaco nei tenant.
 */
public record MedicineVisibilityRequestDto(boolean visibleForTenants) {
}