package com.qtm.ticket.service;

import com.qtm.ticket.dto.CityDto;
import com.qtm.ticket.entity.CityEntity;
import com.qtm.ticket.mapper.CityMapper;
import com.qtm.ticket.repository.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service per la gestione delle città.
 */
@Service
@RequiredArgsConstructor
public class CityService {

    private final CityRepository cityRepository;
    private final CityMapper cityMapper;

    public List<CityDto> findAll() {
        return cityRepository.findAllByOrderByName().stream()
                .map(cityMapper::entityToDto)
                .toList();
    }

    public List<CityDto> findByProvinceId(Long provinceId) {
        return cityRepository.findByProvinceId(provinceId).stream()
                .map(cityMapper::entityToDto)
                .toList();
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
