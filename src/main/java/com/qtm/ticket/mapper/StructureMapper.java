package com.qtm.ticket.mapper;

import org.springframework.stereotype.Component;

import com.qtm.ticket.entity.StructureEntity;
import com.qtm.ticket.entity.StructureTypeEntity;
import com.qtm.ticket.dto.StructureDto;

/**
 * Mapper per StructureEntity e StructureDto.
 */
@Component
public class StructureMapper {

    public StructureDto entityToDto(StructureEntity entity) {
        if (entity == null) {
            return null;
        }
        StructureTypeEntity type = entity.getStructureType();
        return StructureDto.builder()
                .id(entity.getId())
                .codiceRegione(entity.getCodiceRegione())
                .codiceAzienda(entity.getCodiceAzienda())
                .codiceStruttura(entity.getCodiceStruttura())
                .denominazioneStruttura(entity.getDenominazioneStruttura())
                .indirizzo(entity.getIndirizzo())
                .codiceComune(entity.getCodiceComune())
                .comune(entity.getComune())
                .siglaProvincia(entity.getSiglaProvincia())
                .cityId(entity.getCityId())
                .structureTypeCode(type != null ? type.getCode() : null)
                .structureTypeDescription(type != null ? type.getDescription() : null)
                .build();
    }

    public StructureEntity dtoToEntity(StructureDto dto) {
        if (dto == null) {
            return null;
        }
        StructureTypeEntity type = null;
        if (dto.getStructureTypeCode() != null) {
            type = StructureTypeEntity.builder().code(dto.getStructureTypeCode()).build();
        }
        return StructureEntity.builder()
                .id(dto.getId())
                .codiceRegione(dto.getCodiceRegione())
                .codiceAzienda(dto.getCodiceAzienda())
                .codiceStruttura(dto.getCodiceStruttura())
                .denominazioneStruttura(dto.getDenominazioneStruttura())
                .indirizzo(dto.getIndirizzo())
                .codiceComune(dto.getCodiceComune())
                .comune(dto.getComune())
                .siglaProvincia(dto.getSiglaProvincia())
                .cityId(dto.getCityId())
                .structureType(type)
                .build();
    }
}
