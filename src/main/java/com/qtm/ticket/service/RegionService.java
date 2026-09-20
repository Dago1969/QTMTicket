package com.qtm.ticket.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.qtm.ticket.dto.RegionDto;
import com.qtm.ticket.entity.RegionEntity;
import com.qtm.ticket.geography.StaticGeographyCatalog;
import com.qtm.ticket.mapper.RegionMapper;
import com.qtm.ticket.repository.RegionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service per la gestione delle regioni.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RegionService {

    private final RegionRepository regionRepository;
    private final RegionMapper regionMapper;
    private final StaticGeographyCatalog staticGeographyCatalog;

    public List<RegionDto> findAll() {
        try {
            return regionRepository.findAllByOrderByName().stream()
                    .map(regionMapper::entityToDto)
                    .toList();
        } catch (RuntimeException exception) {
            log.error("[RegionService] Falling back to static geography catalog for regions", exception);
            return staticGeographyCatalog.getRegions();
        }
    }

    public RegionDto findById(Long id) {
        return regionRepository.findById(id)
                .map(regionMapper::entityToDto)
                .orElse(null);
    }

    public RegionDto save(RegionDto dto) {
        RegionEntity entity = regionMapper.dtoToEntity(dto);
        return regionMapper.entityToDto(regionRepository.save(entity));
    }

    public void delete(Long id) {
        regionRepository.deleteById(id);
    }
}
