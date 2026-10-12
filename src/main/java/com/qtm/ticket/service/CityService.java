package com.qtm.ticket.service;

import com.qtm.commonlib.dto.CityDto;
import com.qtm.ticket.client.QtmGeographyClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service per la gestione delle città.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CityService {

    private final QtmGeographyClient geographyClient;

    public List<CityDto> findAll() {
        return geographyClient.findCities();
    }

    public List<CityDto> findByProvinceId(Long provinceId) {
        return geographyClient.findCitiesByProvinceId(provinceId);
    }

    public CityDto findById(Long id) {
        return geographyClient.findCityById(id).getBody();
    }

    public CityDto save(CityDto dto) {
        return dto.getId() == null ? geographyClient.createCity(dto) : geographyClient.updateCity(dto.getId(), dto);
    }

    public void delete(Long id) {
        geographyClient.deleteCity(id);
    }
}
