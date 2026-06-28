package com.qtm.ticket.mapper;

import org.springframework.stereotype.Component;

import com.qtm.ticket.dto.DisciplinaDto;
import com.qtm.ticket.entity.DisciplinaEntity;

@Component
public class DisciplinaMapper {

    public DisciplinaDto entityToDto(DisciplinaEntity entity) {
        if (entity == null) {
            return null;
        }
        return DisciplinaDto.builder()
                .codiceDisciplina(entity.getCodiceDisciplina())
                .disciplina(entity.getDisciplina())
                .build();
    }

    public DisciplinaEntity dtoToEntity(DisciplinaDto dto) {
        if (dto == null) {
            return null;
        }
        return DisciplinaEntity.builder()
                .codiceDisciplina(dto.getCodiceDisciplina())
                .disciplina(dto.getDisciplina())
                .build();
    }
}
