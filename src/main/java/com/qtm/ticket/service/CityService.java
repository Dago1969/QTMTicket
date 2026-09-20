package com.qtm.ticket.service;

import com.qtm.ticket.dto.CityDto;
import com.qtm.ticket.entity.CityEntity;
import com.qtm.ticket.geography.StaticGeographyCatalog;
import com.qtm.ticket.mapper.CityMapper;
import com.qtm.ticket.repository.CityRepository;
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

    private final CityRepository cityRepository;
    private final CityMapper cityMapper;
    private final StaticGeographyCatalog staticGeographyCatalog;

    public List<CityDto> findAll() {
        return cityRepository.findAllByOrderByName().stream()
                .map(cityMapper::entityToDto)
                .toList();
    }

    public List<CityDto> findByProvinceId(Long provinceId) {
        try {
            return cityRepository.findByProvinceId(provinceId).stream()
                    .map(cityMapper::entityToDto)
                    .toList();
        } catch (RuntimeException exception) {
            log.error("[CityService] Falling back to static geography catalog for provinceId={}", provinceId, exception);
            return staticGeographyCatalog.findCitiesByProvinceId(provinceId);
        }
    }

    public CityDto findById(Long id) {
        return cityRepository.findById(id)
                .map(cityMapper::entityToDto)
                .orElse(null);
    }

    public CityDto save(CityDto dto) {
        CityEntity entity = cityMapper.dtoToEntity(dto);
        return cityMapper.entityToDto(cityRepository.save(entity));
    }

    public void delete(Long id) {
        cityRepository.deleteById(id);
    }
}
