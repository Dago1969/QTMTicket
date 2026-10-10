package com.qtm.ticket.drug.service;

import com.qtm.commonlib.dto.FarmacoDto;
import com.qtm.ticket.service.MedicineService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FarmadatiServiceTest {

    @Mock
    private MedicineService medicineService;

    @InjectMocks
    private FarmadatiService farmadatiService;

    @Test
    void searchShouldIgnoreQueriesShorterThanThreeCharacters() {
        assertEquals(List.of(), farmadatiService.search(" ab "));
        verifyNoInteractions(medicineService);
    }

    @Test
    void searchShouldSearchTheLocalCatalogWithTrimmedQuery() {
        FarmacoDto medicine = new FarmacoDto();
        when(medicineService.searchVisibleForTenants("paracetamolo")).thenReturn(List.of(medicine));

        assertEquals(List.of(medicine), farmadatiService.search("  paracetamolo  "));
        verify(medicineService).searchVisibleForTenants("paracetamolo");
    }
}