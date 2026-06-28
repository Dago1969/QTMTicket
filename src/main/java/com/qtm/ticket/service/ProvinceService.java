package com.qtm.ticket.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.qtm.ticket.dto.ProvinceDto;
import com.qtm.ticket.entity.ProvinceEntity;
import com.qtm.ticket.mapper.ProvinceMapper;
import com.qtm.ticket.repository.ProvinceRepository;

import lombok.RequiredArgsConstructor;

/**
 * Service per la gestione delle province.
 */
@Service
@RequiredArgsConstructor
public class ProvinceService {

    private final ProvinceRepository provinceRepository;
    private final ProvinceMapper provinceMapper;

    public List<ProvinceDto> findAll() {
        return provinceRepository.findAllByOrderByName().stream()
                .map(provinceMapper::entityToDto)
                .toList();
    }

    public List<ProvinceDto> findByRegionId(Long regionId) {
        return provinceRepository.findByRegionId(regionId).stream()
                .map(provinceMapper::entityToDto)
                .toList();
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
