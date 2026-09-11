package com.qtm.ticket.mapper;

import org.springframework.stereotype.Component;

import com.qtm.commonlib.dto.ASLDto;
import com.qtm.ticket.entity.ASLEntity;
import com.qtm.ticket.entity.CityEntity;

/**
 * Mapper per ASLEntity e ASLDto.
 */
@Component
public class ASLMapper {

    public ASLDto entityToDto(ASLEntity entity) {
        if (entity == null) {
            return null;
        }
        return ASLDto.builder()
                .id(entity.getId())
            .anno(entity.getAnno())
                .codiceAzienda(entity.getCodiceAzienda())
                .denominazioneAzienda(entity.getDenominazioneAzienda())
                .codiceRegione(entity.getCodiceRegione())
                .cityId(entity.getCity() != null ? entity.getCity().getId() : null)
                .indirizzo(entity.getIndirizzo())
                .cap(entity.getCap())
                .telefono(entity.getTelefono())
                .fax(entity.getFax())
                .email(entity.getEmail())
                .sitoWeb(entity.getSitoWeb())
                .partitaIva(entity.getPartitaIva())
                .build();
    }

    public ASLEntity dtoToEntity(ASLDto dto) {
        if (dto == null) {
            return null;
        }
        CityEntity city = null;
        if (dto.getCityId() != null) {
            city = CityEntity.builder().id(dto.getCityId()).build();
        }
        return ASLEntity.builder()
                .id(dto.getId())
            .anno(dto.getAnno())
                .codiceAzienda(dto.getCodiceAzienda())
                .denominazioneAzienda(dto.getDenominazioneAzienda())
            .codiceRegione(dto.getCodiceRegione())
                .city(city)
                .indirizzo(dto.getIndirizzo())
                .cap(dto.getCap())
                .telefono(dto.getTelefono())
                .fax(dto.getFax())
                .email(dto.getEmail())
                .sitoWeb(dto.getSitoWeb())
                .partitaIva(dto.getPartitaIva())
                .build();
    }
}
