package com.qtm.ticket.mapper;

import org.springframework.stereotype.Component;

import com.qtm.ticket.dto.StructureDepartmentDto;
import com.qtm.ticket.entity.DisciplinaEntity;
import com.qtm.ticket.entity.HospitalEntity;
import com.qtm.ticket.entity.StructureDepartmentEntity;

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
            .disciplina(entity.getDisciplina() != null ? entity.getDisciplina().getDisciplina() : null)
                .indirizzo(entity.getIndirizzo())
                .build();
    }

    public StructureDepartmentEntity dtoToEntity(StructureDepartmentDto dto) {
        if (dto == null) {
            return null;
        }
        return StructureDepartmentEntity.builder()
                .id(dto.getId())
            .structure(HospitalEntity.builder().codiceStruttura(dto.getCodiceStruttura()).build())
                .disciplina(DisciplinaEntity.builder()
                        .codiceDisciplina(dto.getCodiceDisciplina())
                        .disciplina(dto.getDisciplina())
                        .build())
                .indirizzo(dto.getIndirizzo())
                .build();
    }
}
