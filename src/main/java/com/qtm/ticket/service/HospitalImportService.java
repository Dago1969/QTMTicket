package com.qtm.ticket.service;

import com.qtm.ticket.entity.ASLEntity;
import com.qtm.ticket.entity.CityEntity;
import com.qtm.ticket.entity.HospitalEntity;
import com.qtm.ticket.entity.HospitalTypeEntity;
import com.qtm.ticket.repository.ASLRepository;
import com.qtm.ticket.repository.CityRepository;
import com.qtm.ticket.repository.HospitalRepository;
import com.qtm.ticket.repository.HospitalTypeRepository;
import com.qtm.ticket.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Importa il catalogo ospedali da CSV mantenendo per ogni struttura solo il record dell'anno piu' recente.
 */
@Service
@RequiredArgsConstructor
public class HospitalImportService {

    private final HospitalRepository hospitalRepository;
    private final HospitalTypeRepository hospitalTypeRepository;
    private final ASLRepository aslRepository;
    private final CityRepository cityRepository;
    private final RegionRepository regionRepository;

    @Transactional
    public void importCsv(InputStream inputStream) {
        Map<String, HospitalCsvRecordDto> latestRecords = loadLatestRecords(inputStream);
        List<HospitalEntity> entitiesToSave = new ArrayList<>();
        Map<String, CityEntity> cityCache = new HashMap<>();

        for (HospitalCsvRecordDto record : latestRecords.values()) {
            if (record.getAnno() == null) {
                continue;
            }

            String normalizedRegionCode = normalizeRegionCode(record.getCodiceRegione(), record.getRegione());
            String codiceAsl = trimToNull(record.getCodiceAsl());
            String codiceStruttura = trimToNull(record.getCodiceStruttura());
            String denominazione = trimToNull(record.getDenominazioneStruttura());

            if (normalizedRegionCode == null || codiceAsl == null || codiceStruttura == null || denominazione == null) {
                continue;
            }

            Optional<HospitalEntity> existingEntity = hospitalRepository.findByCodiceAslAndCodiceStruttura(codiceAsl, codiceStruttura);
            if (existingEntity.isPresent() && existingEntity.get().getAnno() != null
                    && existingEntity.get().getAnno() >= record.getAnno()) {
                continue;
            }

            HospitalTypeEntity hospitalType = resolveHospitalType(record);
            ASLEntity aslEntity = aslRepository.findByCodiceRegioneAndCodiceAzienda(normalizedRegionCode, codiceAsl).orElse(null);
            CityEntity city = resolveCity(record, cityCache);

            HospitalEntity entity = buildOrUpdateEntity(existingEntity.orElse(null), record, normalizedRegionCode, hospitalType, aslEntity, city);
            entitiesToSave.add(entity);
        }

        if (entitiesToSave.isEmpty()) {
            return;
        }

        hospitalRepository.saveAll(entitiesToSave);
    }

    private Map<String, HospitalCsvRecordDto> loadLatestRecords(InputStream inputStream) {
        Map<String, HospitalCsvRecordDto> latestRecords = new LinkedHashMap<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String headerLine = reader.readLine();
            if (headerLine == null) {
                return latestRecords;
            }

            Map<String, Integer> headerIndexes = buildHeaderIndexes(parseCsvLine(headerLine));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }

                HospitalCsvRecordDto record = mapLine(parseCsvLine(line), headerIndexes);
                if (record == null) {
                    continue;
                }

                String normalizedRegionCode = normalizeRegionCode(record.getCodiceRegione(), record.getRegione());
                String codiceAsl = trimToNull(record.getCodiceAsl());
                String codiceStruttura = trimToNull(record.getCodiceStruttura());
                if (normalizedRegionCode == null || codiceAsl == null || codiceStruttura == null) {
                    continue;
                }

                String key = normalizedRegionCode + "||" + codiceAsl + "||" + codiceStruttura;
                HospitalCsvRecordDto current = latestRecords.get(key);
                if (current == null || compareYear(record.getAnno(), current.getAnno()) > 0) {
                    latestRecords.put(key, record);
                }
            }
            return latestRecords;
        } catch (IOException exception) {
            throw new RuntimeException("Errore durante la lettura del file CSV ospedali", exception);
        }
    }

    private HospitalCsvRecordDto mapLine(List<String> values, Map<String, Integer> headerIndexes) {
        HospitalCsvRecordDto record = new HospitalCsvRecordDto();
        record.setAnno(parseInteger(valueOf(values, headerIndexes, "anno")));
        record.setCodiceStruttura(firstValue(values, headerIndexes, "codice_struttura"));
        record.setDenominazioneStruttura(firstValue(values, headerIndexes, "denominazione_struttura", "struttura"));
        record.setComune(firstValue(values, headerIndexes, "comune"));
        record.setSiglaProvincia(firstValue(values, headerIndexes, "sigla_provincia_struttura", "sigla_provincia"));
        record.setCodiceTipoStruttura(firstValue(values, headerIndexes, "codice_tipo_struttura"));
        record.setTipoStruttura(firstValue(values, headerIndexes, "tipo_struttura"));
        record.setCodiceRegione(firstValue(values, headerIndexes, "codice_regione"));
        record.setRegione(firstValue(values, headerIndexes, "regione"));
        record.setCodiceAsl(firstValue(values, headerIndexes, "codice_asl"));
        record.setAsl(firstValue(values, headerIndexes, "asl"));
        record.setIndirizzo(firstValue(values, headerIndexes, "indirizzo"));
        if (record.getAnno() == null && trimToNull(record.getCodiceStruttura()) == null) {
            return null;
        }
        return record;
    }

    private HospitalEntity buildOrUpdateEntity(
            HospitalEntity existingEntity,
            HospitalCsvRecordDto record,
            String normalizedRegionCode,
            HospitalTypeEntity hospitalType,
            ASLEntity aslEntity,
            CityEntity city) {
        HospitalEntity entity = existingEntity != null ? existingEntity : new HospitalEntity();
        entity.setAnno(record.getAnno());
        entity.setCodiceRegione(normalizedRegionCode);
        entity.setRegione(trimToNull(record.getRegione()));
        entity.setCodiceAsl(trimToNull(record.getCodiceAsl()));
        entity.setAslDescrizione(trimToNull(record.getAsl()));
        entity.setCodiceStruttura(trimToNull(record.getCodiceStruttura()));
        entity.setStruttura(trimToNull(record.getDenominazioneStruttura()));
        entity.setComune(trimToNull(record.getComune()));
        entity.setSiglaProvincia(trimToNull(record.getSiglaProvincia()));
        entity.setIndirizzo(trimToNull(record.getIndirizzo()));
        entity.setTipoStruttura(trimToNull(record.getTipoStruttura()));
        entity.setHospitalType(hospitalType);
        entity.setAsl(aslEntity);
        entity.setCity(city);
        return entity;
    }

    private HospitalTypeEntity resolveHospitalType(HospitalCsvRecordDto record) {
        String code = trimToNull(record.getCodiceTipoStruttura());
        String description = trimToNull(record.getTipoStruttura());
        if (code == null && description == null) {
            return null;
        }
        if (code == null) {
            return null;
        }

        HospitalTypeEntity hospitalType = hospitalTypeRepository.findByCode(code)
                .orElseGet(() -> HospitalTypeEntity.builder().code(code).build());
        hospitalType.setDescription(description);
        return hospitalTypeRepository.save(hospitalType);
    }

    private CityEntity resolveCity(HospitalCsvRecordDto record, Map<String, CityEntity> cityCache) {
        String comune = trimToNull(record.getComune());
        String siglaProvincia = trimToNull(record.getSiglaProvincia());
        if (comune == null || siglaProvincia == null) {
            return null;
        }

        String key = comune.toLowerCase(Locale.ROOT) + "||" + siglaProvincia.toLowerCase(Locale.ROOT);
        return cityCache.computeIfAbsent(key, ignored -> cityRepository
                .findFirstByNameIgnoreCaseAndProvince_SiglaIgnoreCase(comune, siglaProvincia)
                .orElse(null));
    }

    private Map<String, Integer> buildHeaderIndexes(List<String> headers) {
        Map<String, Integer> headerIndexes = new HashMap<>();
        for (int index = 0; index < headers.size(); index++) {
            headerIndexes.put(normalizeHeader(headers.get(index)), index);
        }
        return headerIndexes;
    }

    private List<String> parseCsvLine(String line) {
        List<String> values = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (int index = 0; index < line.length(); index++) {
            char currentChar = line.charAt(index);
            if (currentChar == '"') {
                if (inQuotes && index + 1 < line.length() && line.charAt(index + 1) == '"') {
                    current.append('"');
                    index++;
                } else {
                    inQuotes = !inQuotes;
                }
                continue;
            }

            if (currentChar == ';' && !inQuotes) {
                values.add(current.toString());
                current.setLength(0);
                continue;
            }

            current.append(currentChar);
        }

        values.add(current.toString());
        return values;
    }

    private String firstValue(List<String> values, Map<String, Integer> headerIndexes, String... keys) {
        for (String key : keys) {
            String value = valueOf(values, headerIndexes, key);
            if (trimToNull(value) != null) {
                return value;
            }
        }
        return null;
    }

    private String valueOf(List<String> values, Map<String, Integer> headerIndexes, String key) {
        Integer index = headerIndexes.get(key);
        if (index == null || index < 0 || index >= values.size()) {
            return null;
        }
        return values.get(index);
    }

    private Integer parseInteger(String value) {
        String trimmed = trimToNull(value);
        if (trimmed == null) {
            return null;
        }
        try {
            return Integer.parseInt(trimmed);
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    private int compareYear(Integer left, Integer right) {
        if (Objects.equals(left, right)) {
            return 0;
        }
        if (left == null) {
            return -1;
        }
        if (right == null) {
            return 1;
        }
        return Integer.compare(left, right);
    }

    private String normalizeRegionCode(String rawCode, String regionName) {
        String trimmedCode = trimToNull(rawCode);
        if (trimmedCode != null) {
            String digitsOnly = trimmedCode.replaceAll("[^0-9]", "");
            List<String> candidates = new ArrayList<>();
            if (!digitsOnly.isBlank()) {
                if (digitsOnly.length() >= 2) {
                    candidates.add(digitsOnly.substring(0, 2));
                }
                if (digitsOnly.length() == 1) {
                    candidates.add("0" + digitsOnly);
                }
            }
            candidates.add(trimmedCode);

            for (String candidate : candidates.stream().filter(Objects::nonNull).map(String::trim).filter(value -> !value.isBlank()).distinct().toList()) {
                if (regionRepository.findByCode(candidate).isPresent()) {
                    return candidate;
                }
            }
        }

        String normalizedRegionName = trimToNull(regionName);
        if (normalizedRegionName == null) {
            return null;
        }
        return regionRepository.findFirstByNameIgnoreCase(normalizedRegionName)
                .map(region -> region.getCode())
                .orElse(null);
    }

    private String normalizeHeader(String header) {
        String trimmed = header == null ? "" : header.trim().toLowerCase(Locale.ROOT);
        return trimmed.replaceAll("[^a-z0-9]+", "_").replaceAll("^_+|_+$", "");
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}