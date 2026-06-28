package com.qtm.ticket.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.qtm.ticket.dto.RegionDto;
import com.qtm.ticket.entity.RegionEntity;
import com.qtm.ticket.mapper.RegionMapper;
import com.qtm.ticket.repository.RegionRepository;

import lombok.RequiredArgsConstructor;

/**
 * Service per la gestione delle regioni.
 */
@Service
@RequiredArgsConstructor
public class RegionService {

    private final RegionRepository regionRepository;
    private final RegionMapper regionMapper;

    public List<RegionDto> findAll() {
        return regionRepository.findAllByOrderByName().stream()
                .map(regionMapper::entityToDto)
                .toList();
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
