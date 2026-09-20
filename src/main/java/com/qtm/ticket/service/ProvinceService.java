package com.qtm.ticket.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.qtm.ticket.dto.ProvinceDto;
import com.qtm.ticket.entity.ProvinceEntity;
import com.qtm.ticket.geography.StaticGeographyCatalog;
import com.qtm.ticket.mapper.ProvinceMapper;
import com.qtm.ticket.repository.ProvinceRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service per la gestione delle province.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProvinceService {

    private final ProvinceRepository provinceRepository;
    private final ProvinceMapper provinceMapper;
    private final StaticGeographyCatalog staticGeographyCatalog;

    public List<ProvinceDto> findAll() {
        return provinceRepository.findAllByOrderByName().stream()
                .map(provinceMapper::entityToDto)
                .toList();
    }

    public List<ProvinceDto> findByRegionId(Long regionId) {
        try {
            return provinceRepository.findByRegionId(regionId).stream()
                    .map(provinceMapper::entityToDto)
                    .toList();
        } catch (RuntimeException exception) {
            log.error("[ProvinceService] Falling back to static geography catalog for regionId={}", regionId, exception);
            return staticGeographyCatalog.findProvincesByRegionId(regionId);
        }
    }

    public ProvinceDto findById(Long id) {
        return provinceRepository.findById(id)
                .map(provinceMapper::entityToDto)
                .orElse(null);
    }

    public ProvinceDto save(ProvinceDto dto) {
        ProvinceEntity entity = provinceMapper.dtoToEntity(dto);
        return provinceMapper.entityToDto(provinceRepository.save(entity));
    }

    public void delete(Long id) {
        provinceRepository.deleteById(id);
    }
}
