package com.qtm.ticket.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.qtm.ticket.dto.StructureDepartmentDto;
import com.qtm.ticket.entity.StructureDepartmentEntity;
import com.qtm.ticket.mapper.StructureDepartmentMapper;
import com.qtm.ticket.repository.DisciplinaRepository;
import com.qtm.ticket.repository.StructureDepartmentRepository;
import com.qtm.ticket.repository.StructureRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StructureDepartmentService {

    private final StructureDepartmentRepository structureDepartmentRepository;
    private final StructureDepartmentMapper structureDepartmentMapper;
    private final StructureRepository structureRepository;
    private final DisciplinaRepository disciplinaRepository;

    public List<StructureDepartmentDto> findAll() {
        return structureDepartmentRepository.findAll().stream()
                .map(structureDepartmentMapper::entityToDto)
                .toList();
    }

    public List<StructureDepartmentDto> findByStructure(String codiceStruttura) {
        return structureDepartmentRepository.findByStructure_CodiceStruttura(codiceStruttura).stream()
                .map(structureDepartmentMapper::entityToDto)
                .toList();
    }

    public StructureDepartmentDto save(StructureDepartmentDto dto) {
        StructureDepartmentEntity entity = structureDepartmentMapper.dtoToEntity(dto);
        if (dto.getCodiceStruttura() != null) {
            structureRepository.findByCodiceStruttura(dto.getCodiceStruttura())
                    .ifPresent(entity::setStructure);
        }
        if (dto.getCodiceDisciplina() != null) {
            disciplinaRepository.findById(dto.getCodiceDisciplina())
                    .ifPresent(entity::setDisciplina);
        }
        return structureDepartmentMapper.entityToDto(structureDepartmentRepository.save(entity));
    }

    public boolean deleteByCodes(String codiceStruttura, String codiceDisciplina) {
        var existing = structureDepartmentRepository
                .findByStructure_CodiceStrutturaAndDisciplina_CodiceDisciplina(codiceStruttura, codiceDisciplina);
        if (existing.isPresent()) {
            structureDepartmentRepository.deleteByStructure_CodiceStrutturaAndDisciplina_CodiceDisciplina(codiceStruttura,
                    codiceDisciplina);
            return true;
        }
        return false;
    }
}
