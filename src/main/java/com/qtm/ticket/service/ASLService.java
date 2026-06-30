package com.qtm.ticket.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
@Transactional
public class ASLService {

    private final ASLRepository aslRepository;
    private final ASLMapper aslMapper;

    @Transactional(readOnly = true)
    public List<ASLDto> findAll() {
        return aslRepository.findAll().stream()
                .map(aslMapper::entityToDto)
                .toList();
    }

    @Transactional(readOnly = true)
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
