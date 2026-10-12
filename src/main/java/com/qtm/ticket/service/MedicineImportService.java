package com.qtm.ticket.service;

import com.qtm.ticket.dto.MedicineImportResultDto;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_GATEWAY;
import static org.springframework.http.MediaType.parseMediaType;

/**
 * Scarica il CSV AIFA in streaming e aggiorna il catalogo centrale per codice AIC.
 */
@Service
@RequiredArgsConstructor
public class MedicineImportService {

    private static final int BATCH_SIZE = 500;
    private static final CSVFormat AIFA_FORMAT = CSVFormat.DEFAULT.builder()
            .setDelimiter(';')
            .setHeader()
            .setSkipHeaderRecord(true)
            .setIgnoreEmptyLines(true)
            .setTrim(true)
            .build();
    private static final String UPSERT_SQL = """
            INSERT INTO medicines (
                codice_aic, cod_farmaco, cod_confezione, denominazione, descrizione,
                codice_ditta, ragione_sociale, stato_amministrativo, tipo_procedura,
                forma, codice_atc, pa_associati, fornitura, link_fi, link_rcp
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            ON DUPLICATE KEY UPDATE
                cod_farmaco = VALUES(cod_farmaco), cod_confezione = VALUES(cod_confezione),
                denominazione = VALUES(denominazione), descrizione = VALUES(descrizione),
                codice_ditta = VALUES(codice_ditta), ragione_sociale = VALUES(ragione_sociale),
                stato_amministrativo = VALUES(stato_amministrativo), tipo_procedura = VALUES(tipo_procedura),
                forma = VALUES(forma), codice_atc = VALUES(codice_atc), pa_associati = VALUES(pa_associati),
                fornitura = VALUES(fornitura), link_fi = VALUES(link_fi), link_rcp = VALUES(link_rcp)
            """;

    private final JdbcTemplate jdbcTemplate;
    private final RestClient.Builder restClientBuilder;

    @Value("${medicines.import.source-url:https://drive.aifa.gov.it/farmaci/confezioni_fornitura.csv}")
    private String sourceUrl;

    @Transactional
    public MedicineImportResultDto importFromAifa() {
        return restClientBuilder.build().get()
                .uri(sourceUrl)
                .accept(parseMediaType("text/csv"))
                .exchange((request, response) -> {
                    if (!response.getStatusCode().is2xxSuccessful()) {
                        throw new ResponseStatusException(BAD_GATEWAY, "Download del catalogo AIFA non riuscito");
                    }
                    return processCsv(response.getBody());
                });
    }

    private MedicineImportResultDto processCsv(InputStream input) {
        long importedRows = 0;
        long skippedRows = 0;
        List<Object[]> batch = new ArrayList<>(BATCH_SIZE);
        try (CSVParser parser = CSVParser.parse(input, StandardCharsets.UTF_8, AIFA_FORMAT)) {
            for (CSVRecord record : parser) {
                String aicCode = value(record, "CODICE_AIC");
                if (aicCode.isBlank()) {
                    skippedRows++;
                    continue;
                }
                batch.add(toParameters(record, aicCode));
                if (batch.size() == BATCH_SIZE) {
                    persistBatch(batch);
                    importedRows += batch.size();
                    batch.clear();
                }
            }
            if (!batch.isEmpty()) {
                persistBatch(batch);
                importedRows += batch.size();
            }
            return new MedicineImportResultDto(importedRows, skippedRows);
        } catch (IOException | IllegalArgumentException exception) {
            throw new ResponseStatusException(BAD_GATEWAY, "Impossibile leggere il CSV del catalogo AIFA", exception);
        }
    }

    private void persistBatch(List<Object[]> batch) {
        jdbcTemplate.batchUpdate(UPSERT_SQL, batch, BATCH_SIZE, (statement, parameters) -> {
            for (int parameterIndex = 0; parameterIndex < parameters.length; parameterIndex++) {
                statement.setObject(parameterIndex + 1, parameters[parameterIndex]);
            }
        });
    }

    private Object[] toParameters(CSVRecord record, String aicCode) {
        return new Object[]{aicCode, value(record, "COD_FARMACO"), value(record, "COD_CONFEZIONE"),
                value(record, "DENOMINAZIONE"), value(record, "DESCRIZIONE"), value(record, "CODICE_DITTA"),
                value(record, "RAGIONE_SOCIALE"), value(record, "STATO_AMMINISTRATIVO"),
                value(record, "TIPO_PROCEDURA"), value(record, "FORMA"), value(record, "CODICE_ATC"),
                value(record, "PA_ASSOCIATI"), value(record, "FORNITURA"), value(record, "LINK_FI"),
                value(record, "LINK_RCP")};
    }

    private String value(CSVRecord record, String column) {
        return record.isMapped(column) ? record.get(column).trim() : "";
    }
}