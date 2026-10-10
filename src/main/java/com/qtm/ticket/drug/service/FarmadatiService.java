package com.qtm.ticket.drug.service;

import com.qtm.commonlib.dto.FarmacoDto;
import com.qtm.ticket.service.MedicineService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Facade di ricerca farmaci per l'autocomplete sul catalogo centralizzato QTMTicket.
 */
@Service
@RequiredArgsConstructor
public class FarmadatiService {

    private static final int MIN_QUERY_LENGTH = 3;
    private final MedicineService medicineService;

    public List<FarmacoDto> search(String query) {
        String normalizedQuery = query == null ? "" : query.trim();
        if (normalizedQuery.length() < MIN_QUERY_LENGTH) {
            return List.of();
        }
        return medicineService.searchVisibleForTenants(normalizedQuery);
    }
}