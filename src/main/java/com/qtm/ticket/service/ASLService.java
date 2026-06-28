package com.qtm.ticket.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.qtm.commonlib.dto.ASLDto;
import com.qtm.ticket.entity.ASLEntity;
import com.qtm.ticket.mapper.ASLMapper;
import com.qtm.ticket.repository.ASLRepository;

import lombok.RequiredArgsConstructor;

/**
 * Service per la gestione dei record ASL.
 */
@Service
@RequiredArgsConstructor
public class ASLService {

    private final ASLRepository aslRepository;
    private final ASLMapper aslMapper;

    public List<ASLDto> findAll() {
        return aslRepository.findAll().stream()
                .map(aslMapper::entityToDto)
                .toList();
    }

    public ASLDto findById(Long id) {
        return aslRepository.findById(id)
                .map(aslMapper::entityToDto)
                .orElse(null);
    }

    public ASLDto save(ASLDto dto) {
        ASLEntity entity = aslMapper.dtoToEntity(dto);
        return aslMapper.entityToDto(aslRepository.save(entity));
    }

    public void delete(Long id) {
        aslRepository.deleteById(id);
    }
}
