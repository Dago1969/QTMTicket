package com.qtm.ticket.mapper;

import org.springframework.stereotype.Component;

import com.qtm.ticket.dto.StructureDepartmentDto;
import com.qtm.ticket.entity.DisciplinaEntity;
import com.qtm.ticket.entity.StructureDepartmentEntity;
import com.qtm.ticket.entity.StructureEntity;

@Component
public class StructureDepartmentMapper {

    public StructureDepartmentDto entityToDto(StructureDepartmentEntity entity) {
        if (entity == null) {
            return null;
        }
        return StructureDepartmentDto.builder()
                .id(entity.getId())
                .codiceStruttura(entity.getStructure() != null ? entity.getStructure().getCodiceStruttura() : null)
                .codiceDisciplina(entity.getDisciplina() != null ? entity.getDisciplina().getCodiceDisciplina() : null)
                .indirizzo(entity.getIndirizzo())
                .build();
    }

    public StructureDepartmentEntity dtoToEntity(StructureDepartmentDto dto) {
        if (dto == null) {
            return null;
        }
        return StructureDepartmentEntity.builder()
                .id(dto.getId())
                .structure(StructureEntity.builder().codiceStruttura(dto.getCodiceStruttura()).build())
                .disciplina(DisciplinaEntity.builder().codiceDisciplina(dto.getCodiceDisciplina()).build())
                .indirizzo(dto.getIndirizzo())
                .build();
    }
}
