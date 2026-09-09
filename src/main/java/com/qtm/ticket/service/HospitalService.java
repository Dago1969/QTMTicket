package com.qtm.ticket.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qtm.commonlib.dto.HospitalDto;
import com.qtm.ticket.mapper.HospitalMapper;
import com.qtm.ticket.repository.HospitalRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class HospitalService {

    private final HospitalRepository hospitalRepository;
    private final HospitalMapper hospitalMapper;

    @Transactional(readOnly = true)
    public List<HospitalDto> findAll() {
        return hospitalRepository.findAll().stream()
                .map(hospitalMapper::entityToDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public HospitalDto findById(Long id) {
        return hospitalRepository.findById(id).map(hospitalMapper::entityToDto).orElse(null);
    }

    public HospitalDto save(HospitalDto dto) {
        var entity = hospitalMapper.dtoToEntity(dto);
        return hospitalMapper.entityToDto(hospitalRepository.save(entity));
    }

    public void delete(Long id) {
        hospitalRepository.deleteById(id);
    }
}
