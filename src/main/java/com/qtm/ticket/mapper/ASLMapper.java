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
                .codiceRegione(entity.getCodiceRegione())
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
                .codiceAzienda(dto.getCodiceAzienda())
                .denominazioneAzienda(dto.getDenominazioneAzienda())
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
