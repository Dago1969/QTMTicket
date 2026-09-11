package com.qtm.ticket.mapper;

import org.springframework.stereotype.Component;

import com.qtm.commonlib.dto.HospitalDto;
import com.qtm.ticket.entity.HospitalEntity;
import com.qtm.ticket.entity.HospitalTypeEntity;

@Component
public class HospitalMapper {

    private String firstNonBlank(String... values) {
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value;
            }
        }
        return null;
    }

    public HospitalDto entityToDto(HospitalEntity entity) {
        if (entity == null) return null;
        return HospitalDto.builder()
            .id(entity.getId())
            .anno(entity.getAnno())
            .codiceRegione(entity.getCodiceRegione())
                .regione(entity.getRegione())
                .codiceAsl(entity.getCodiceAsl())
                .asl(firstNonBlank(entity.getAslDescrizione(), entity.getAsl() != null ? entity.getAsl().getDenominazioneAzienda() : null))
                .codiceStruttura(entity.getCodiceStruttura())
                .struttura(entity.getStruttura())
                .tipoStruttura(firstNonBlank(entity.getTipoStruttura(), entity.getHospitalType() != null ? entity.getHospitalType().getDescription() : null))
                .indirizzo(entity.getIndirizzo())
                .hospitalTypeId(entity.getHospitalType() != null ? entity.getHospitalType().getId() : null)
                .aslId(entity.getAsl() != null ? entity.getAsl().getId() : null)
                .comune(firstNonBlank(entity.getComune(), entity.getCity() != null ? entity.getCity().getName() : null))
                .cityId(entity.getCity() != null ? entity.getCity().getId() : null)
                .siglaProvincia(firstNonBlank(entity.getSiglaProvincia(), entity.getCity() != null && entity.getCity().getProvince() != null ? entity.getCity().getProvince().getSigla() : null))
                .build();
    }

    public HospitalEntity dtoToEntity(HospitalDto dto) {
        if (dto == null) return null;
        HospitalTypeEntity ht = null;
        if (dto.getHospitalTypeId() != null) ht = HospitalTypeEntity.builder().id(dto.getHospitalTypeId()).build();
        com.qtm.ticket.entity.ASLEntity asl = null;
        if (dto.getAslId() != null) asl = com.qtm.ticket.entity.ASLEntity.builder().id(dto.getAslId()).build();
        com.qtm.ticket.entity.CityEntity city = null;
        if (dto.getCityId() != null) city = com.qtm.ticket.entity.CityEntity.builder().id(dto.getCityId()).build();

        return HospitalEntity.builder()
            .id(dto.getId())
            .anno(dto.getAnno())
            .codiceRegione(dto.getCodiceRegione())
                .regione(dto.getRegione())
                .codiceAsl(dto.getCodiceAsl())
                .aslDescrizione(dto.getAsl())
                .codiceStruttura(dto.getCodiceStruttura())
                .struttura(dto.getStruttura())
                .comune(dto.getComune())
                .siglaProvincia(dto.getSiglaProvincia())
                .indirizzo(dto.getIndirizzo())
                .tipoStruttura(dto.getTipoStruttura())
                .hospitalType(ht)
                .asl(asl)
                .city(city)
                .build();
    }
}
