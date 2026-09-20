package com.qtm.ticket.geography;

import com.qtm.ticket.dto.CityDto;
import com.qtm.ticket.dto.ProvinceDto;
import com.qtm.ticket.dto.RegionDto;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Catalogo geografia di fallback costruito dal CSV versionato nel progetto.
 * Serve a mantenere operativi gli endpoint regioni/province/citta anche quando il DB geografia non e disponibile.
 */
@Component
@Slf4j
public class StaticGeographyCatalog {

    private static final String GEODATA_RESOURCE = "db/changelog/geodata-cleaned.csv";
    private static final String SEPARATOR = ";";

    @Getter
    private List<RegionDto> regions = List.of();

    private Map<Long, List<ProvinceDto>> provincesByRegionId = Map.of();
    private Map<Long, List<CityDto>> citiesByProvinceId = Map.of();

    @PostConstruct
    void initialize() {
        try {
            loadCatalog();
            log.info("[StaticGeographyCatalog] Loaded fallback geography catalog regions={} provinceGroups={} cityGroups={}",
                    regions.size(), provincesByRegionId.size(), citiesByProvinceId.size());
        } catch (IOException exception) {
            log.error("[StaticGeographyCatalog] Unable to load fallback geography catalog from {}", GEODATA_RESOURCE, exception);
            regions = List.of();
            provincesByRegionId = Map.of();
            citiesByProvinceId = Map.of();
        }
    }

    public List<ProvinceDto> findProvincesByRegionId(Long regionId) {
        return provincesByRegionId.getOrDefault(regionId, List.of());
    }

    public List<CityDto> findCitiesByProvinceId(Long provinceId) {
        return citiesByProvinceId.getOrDefault(provinceId, List.of());
    }

    private void loadCatalog() throws IOException {
        ClassPathResource resource = new ClassPathResource(GEODATA_RESOURCE);
        Map<String, RegionAccumulator> regionAccumulators = new LinkedHashMap<>();
        Map<String, ProvinceAccumulator> provinceAccumulators = new LinkedHashMap<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            String line = reader.readLine();
            if (line == null) {
                return;
            }

            long[] nextRegionId = {1L};
            long[] nextProvinceId = {1L};
            long nextCityId = 1L;

            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }

                String[] columns = line.split(SEPARATOR, -1);
                if (columns.length < 20) {
                    continue;
                }

                String regionCode = sanitize(columns[0]);
                String regionName = sanitize(columns[10]);
                String geographicArea = sanitize(columns[9]);
                String regionType = sanitize(columns[12]);
                String provinceCode = sanitize(columns[2]);
                String provinceName = sanitize(columns[11]);
                String provinceSigla = sanitize(columns[14]);
                String cityCode = sanitize(columns[4]);
                String cityName = sanitize(columns[5]);
                String istatCode = sanitize(columns[15]);
                String catastaleCode = sanitize(columns[19]);
                boolean capoluogo = "1".equals(sanitize(columns[13]));

                if (regionCode.isBlank() || regionName.isBlank()) {
                    continue;
                }

                RegionAccumulator regionAccumulator = regionAccumulators.computeIfAbsent(regionCode, key -> {
                        long regionId = nextRegionId[0]++;
                        return new RegionAccumulator(RegionDto.builder()
                        .id(regionId)
                                .code(regionCode)
                                .name(regionName)
                                .geographicArea(geographicArea)
                                .regionType(regionType)
                        .build());
                    });

                if (provinceCode.isBlank() || provinceName.isBlank()) {
                    continue;
                }

                String provinceMapKey = regionCode + '|' + provinceCode;
                ProvinceAccumulator provinceAccumulator = provinceAccumulators.computeIfAbsent(provinceMapKey, key -> {
                    long provinceId = nextProvinceId[0]++;
                    ProvinceDto province = ProvinceDto.builder()
                            .id(provinceId)
                            .regionId(regionAccumulator.region().getId())
                            .code(provinceCode)
                            .name(provinceName)
                            .sigla(provinceSigla)
                            .build();
                    return new ProvinceAccumulator(province, new ArrayList<>());
                });

                if (cityCode.isBlank() || cityName.isBlank()) {
                    continue;
                }

                boolean alreadyAdded = provinceAccumulator.cities().stream()
                        .anyMatch(city -> Objects.equals(city.getCode(), cityCode));
                if (alreadyAdded) {
                    continue;
                }

                provinceAccumulator.cities().add(CityDto.builder()
                        .id(nextCityId++)
                        .provinceId(provinceAccumulator.province().getId())
                        .code(cityCode)
                        .istatCode(istatCode)
                        .catastaleCode(catastaleCode)
                        .name(cityName)
                        .capoluogo(capoluogo)
                        .build());
            }
        }

        regions = regionAccumulators.values().stream()
                .map(RegionAccumulator::region)
                .sorted(Comparator.comparing(RegionDto::getName, String.CASE_INSENSITIVE_ORDER))
                .toList();

        provincesByRegionId = provinceAccumulators.values().stream()
                .map(ProvinceAccumulator::province)
                .sorted(Comparator.comparing(ProvinceDto::getName, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.groupingBy(ProvinceDto::getRegionId, LinkedHashMap::new, Collectors.toList()));

        citiesByProvinceId = provinceAccumulators.values().stream()
                .collect(Collectors.toMap(
                        accumulator -> accumulator.province().getId(),
                        accumulator -> accumulator.cities().stream()
                                .sorted(Comparator.comparing(CityDto::getName, String.CASE_INSENSITIVE_ORDER))
                                .toList(),
                        (left, _right) -> left,
                        LinkedHashMap::new
                ));
    }

    private static String sanitize(String value) {
        return value == null ? "" : value.trim();
    }

    private record RegionAccumulator(RegionDto region) {
    }

    private record ProvinceAccumulator(ProvinceDto province, List<CityDto> cities) {
    }
}