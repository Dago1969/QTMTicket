package com.qtm.ticket.service;

import com.qtm.ticket.entity.ASLEntity;
import com.qtm.ticket.entity.HospitalEntity;
import com.qtm.ticket.entity.HospitalTypeEntity;
import com.qtm.ticket.repository.ASLRepository;
import com.qtm.ticket.repository.CityRepository;
import com.qtm.ticket.repository.HospitalRepository;
import com.qtm.ticket.repository.HospitalTypeRepository;
import com.qtm.ticket.repository.RegionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HospitalImportServiceTest {

    @Mock
    private HospitalRepository hospitalRepository;

    @Mock
    private HospitalTypeRepository hospitalTypeRepository;

    @Mock
    private ASLRepository aslRepository;

    @Mock
    private CityRepository cityRepository;

    @Mock
    private RegionRepository regionRepository;

    @InjectMocks
    private HospitalImportService hospitalImportService;

    @Test
    void importCsvShouldPersistLatestYearFromSameFile() {
        // Verifica che, a parita' di struttura, l'import selezioni il record CSV con anno piu' recente.
        when(regionRepository.findByCode("01")).thenReturn(Optional.of(com.qtm.ticket.entity.RegionEntity.builder().id(1L).code("01").name("Piemonte").build()));
        when(hospitalRepository.findByCodiceAslAndCodiceStruttura("201", "010003")).thenReturn(Optional.empty());
        when(aslRepository.findByCodiceRegioneAndCodiceAzienda("01", "201"))
                .thenReturn(Optional.of(ASLEntity.builder().id(10L).codiceRegione("01").codiceAzienda("201").denominazioneAzienda("ASL Torino").build()));
        when(hospitalTypeRepository.findByCode("1")).thenReturn(Optional.empty());
        when(hospitalTypeRepository.save(any(HospitalTypeEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(hospitalRepository.saveAll(any())).thenAnswer(invocation -> invocation.getArgument(0));

        hospitalImportService.importCsv(new ByteArrayInputStream(("""
                anno;codice_struttura;denominazione_struttura;comune;sigla_provincia_struttura;codice_tipo_struttura;Tipo struttura;codice_regione;Regione;codice_asl;asl
                2022;010003;Ospedale Storico;Torino;TO;1;Presidio ASL;01;Piemonte;201;ASL Torino
                2024;010003;Ospedale Aggiornato;Torino;TO;1;Presidio ASL;01;Piemonte;201;ASL Torino
                """).getBytes(StandardCharsets.UTF_8)));

        @SuppressWarnings("unchecked")
        ArgumentCaptor<List<HospitalEntity>> captor = (ArgumentCaptor<List<HospitalEntity>>) (ArgumentCaptor<?>) ArgumentCaptor.forClass(List.class);
        verify(hospitalRepository).saveAll(captor.capture());
        assertThat(captor.getValue()).hasSize(1);
        HospitalEntity savedEntity = captor.getValue().get(0);
        assertThat(savedEntity.getAnno()).isEqualTo(2024);
        assertThat(savedEntity.getStruttura()).isEqualTo("Ospedale Aggiornato");
        assertThat(savedEntity.getCodiceRegione()).isEqualTo("01");
        assertThat(savedEntity.getCodiceAsl()).isEqualTo("201");
    }

    @Test
    void importCsvShouldSkipRowsOlderThanDatabaseVersion() {
        // Verifica che un file con anno meno recente non sovrascriva un record gia' piu' aggiornato nel database.
        when(regionRepository.findByCode("01")).thenReturn(Optional.of(com.qtm.ticket.entity.RegionEntity.builder().id(1L).code("01").name("Piemonte").build()));
        when(hospitalRepository.findByCodiceAslAndCodiceStruttura("201", "010003"))
                .thenReturn(Optional.of(HospitalEntity.builder().id(99L).anno(2025).codiceAsl("201").codiceStruttura("010003").build()));

        hospitalImportService.importCsv(new ByteArrayInputStream(("""
                anno;codice_struttura;denominazione_struttura;comune;sigla_provincia_struttura;codice_tipo_struttura;Tipo struttura;codice_regione;Regione;codice_asl;asl
                2024;010003;Ospedale Vecchio;Torino;TO;1;Presidio ASL;01;Piemonte;201;ASL Torino
                """).getBytes(StandardCharsets.UTF_8)));

        verify(hospitalRepository, never()).saveAll(any());
        verify(hospitalTypeRepository, never()).save(any(HospitalTypeEntity.class));
    }
}