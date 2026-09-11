package com.qtm.ticket.service;

import com.qtm.ticket.entity.ASLEntity;
import com.qtm.ticket.entity.CityEntity;
import com.qtm.ticket.repository.ASLRepository;
import com.qtm.ticket.repository.CityRepository;
import com.qtm.ticket.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ASLImportService {

    private final ASLRepository aslRepository;
    private final CityRepository cityRepository;
    private final RegionRepository regionRepository;

    @Transactional
    public void importCsv(InputStream inputStream) {
        Map<String, AslCsvRecordDto> latestAslMap = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            boolean isHeader = true;

            while ((line = reader.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                AslCsvRecordDto dto = parseCsvLine(line);
                if (dto == null) continue;

                String key = dto.getCodiceRegione() + "||" + dto.getCodiceAzienda();

                if (!latestAslMap.containsKey(key) || dto.getAnno() > latestAslMap.get(key).getAnno()) {
                    latestAslMap.put(key, dto);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Errore durante la lettura del file CSV", e);
        }

        Map<String, CityEntity> cityCache = new HashMap<>();
        List<ASLEntity> entitiesToSave = new ArrayList<>();

        for (AslCsvRecordDto dto : latestAslMap.values()) {
            if (dto.getAnno() == null) {
                continue;
            }

            CityEntity city = null;
            if (dto.getComune() != null && !dto.getComune().isBlank()
                    && dto.getSiglaProvincia() != null && !dto.getSiglaProvincia().isBlank()) {
                String cityKey = dto.getComune() + "||" + dto.getSiglaProvincia();
                city = cityCache.computeIfAbsent(cityKey, key ->
                        cityRepository.findFirstByNameIgnoreCaseAndProvince_SiglaIgnoreCase(
                                dto.getComune().trim(),
                                dto.getSiglaProvincia().trim())
                                .orElse(null));
            }

            String normalizedRegionCode = resolveRegionCode(dto);
            if (normalizedRegionCode == null) {
                continue;
            }

            if (dto.getCodiceAzienda() == null || dto.getCodiceAzienda().isBlank()) {
                continue;
            }

            if (dto.getDenominazioneAzienda() == null || dto.getDenominazioneAzienda().isBlank()) {
                continue;
            }

            Optional<ASLEntity> existingEntity = aslRepository.findByCodiceRegioneAndCodiceAzienda(
                    normalizedRegionCode,
                    dto.getCodiceAzienda().trim());
            if (existingEntity.isPresent() && existingEntity.get().getAnno() != null
                    && existingEntity.get().getAnno() >= dto.getAnno()) {
                continue;
            }

            CityEntity resolvedCity = city;
            ASLEntity entity = buildOrUpdateEntity(existingEntity.orElse(null), dto, normalizedRegionCode, resolvedCity);

            if (entity != null) {
                entitiesToSave.add(entity);
            }
        }

        aslRepository.saveAll(entitiesToSave);
    }

    private AslCsvRecordDto parseCsvLine(String line) {
        String[] tokens = line.split(";", -1);

        if (tokens.length < 14) {
            return null;
        }

        try {
            AslCsvRecordDto dto = new AslCsvRecordDto();
            dto.setAnno(Integer.parseInt(tokens[0].trim()));
            dto.setCodiceRegione(trimToNull(tokens[1]));
            dto.setDenominazioneRegione(tokens[2].trim());
            dto.setCodiceAzienda(tokens[3].trim());
            dto.setDenominazioneAzienda(tokens[4].trim());
            dto.setIndirizzo(tokens[5].trim());
            dto.setCap(tokens[6].trim());
            dto.setComune(tokens[7].trim());
            dto.setSiglaProvincia(tokens[8].trim());
            dto.setTelefono(tokens[9].trim());
            dto.setFax(tokens[10].trim());
            dto.setEmail(tokens[11].trim());
            dto.setSitoWeb(tokens[12].trim());
            dto.setPartitaIva(tokens[13].trim());
            return dto;
        } catch (Exception e) {
            return null;
        }
    }

    private String resolveRegionCode(AslCsvRecordDto dto) {
        String rawCode = trimToNull(dto.getCodiceRegione());
        if (rawCode != null) {
            for (String candidate : buildRegionCodeCandidates(rawCode)) {
                if (regionRepository.findByCode(candidate).isPresent()) {
                    return candidate;
                }
            }
        }

        String regionName = trimToNull(dto.getDenominazioneRegione());
        if (regionName == null) {
            return null;
        }

        return regionRepository.findFirstByNameIgnoreCase(regionName)
                .map(region -> region.getCode())
                .orElse(null);
    }

    private List<String> buildRegionCodeCandidates(String rawCode) {
        String trimmed = rawCode.trim();
        List<String> candidates = new ArrayList<>();
        candidates.add(trimmed);

        if (trimmed.matches("\\d{3}")) {
            candidates.add(trimmed.substring(0, 2));
        }

        if (trimmed.matches("\\d")) {
            candidates.add("0" + trimmed);
        }

        return candidates.stream()
                .filter(candidate -> candidate != null && !candidate.isBlank())
                .distinct()
                .toList();
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }

        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private ASLEntity buildOrUpdateEntity(ASLEntity existingEntity,
                                          AslCsvRecordDto dto,
                                          String normalizedRegionCode,
                                          CityEntity city) {
        ASLEntity entity = existingEntity != null ? existingEntity : new ASLEntity();
        entity.setAnno(dto.getAnno());
        entity.setCodiceAzienda(dto.getCodiceAzienda().trim());
        entity.setDenominazioneAzienda(dto.getDenominazioneAzienda().trim());
        entity.setCodiceRegione(normalizedRegionCode);
        entity.setCity(city);
        entity.setIndirizzo(trimToNull(dto.getIndirizzo()));
        entity.setCap(trimToNull(dto.getCap()));
        entity.setTelefono(trimToNull(dto.getTelefono()));
        entity.setFax(trimToNull(dto.getFax()));
        entity.setEmail(trimToNull(dto.getEmail()));
        entity.setSitoWeb(trimToNull(dto.getSitoWeb()));
        entity.setPartitaIva(trimToNull(dto.getPartitaIva()));
        return entity;
    }
}
