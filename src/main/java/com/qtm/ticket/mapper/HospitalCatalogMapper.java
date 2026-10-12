package com.qtm.ticket.mapper;

import org.springframework.stereotype.Component;

import com.qtm.ticket.dto.HospitalCatalogDto;
import com.qtm.ticket.entity.HospitalCatalogEntity;
import com.qtm.ticket.entity.HospitalTypeEntity;

/** Mapper per la cache ospedali QTMTicket e il relativo DTO. */
@Component
public class HospitalCatalogMapper {

    public HospitalCatalogDto entityToDto(HospitalCatalogEntity entity) {
        if (entity == null) {
            return null;
        }
        HospitalTypeEntity type = entity.getHospitalType();
        return HospitalCatalogDto.builder()
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
                .active(entity.getActive())
                .build();
    }

    public HospitalCatalogEntity dtoToEntity(HospitalCatalogDto dto) {
        if (dto == null) {
            return null;
        }
        HospitalTypeEntity type = null;
        if (dto.getStructureTypeCode() != null) {
            type = HospitalTypeEntity.builder().code(dto.getStructureTypeCode()).build();
        }
        return HospitalCatalogEntity.builder()
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
                .hospitalType(type)
                .active(dto.getActive() != null ? dto.getActive() : false)
                .build();
    }
}