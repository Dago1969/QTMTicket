package com.qtm.ticket.mapper;

import org.springframework.stereotype.Component;

import com.qtm.commonlib.dto.HospitalDto;
import com.qtm.ticket.entity.HospitalEntity;
import com.qtm.ticket.entity.HospitalTypeEntity;

@Component
public class HospitalMapper {

    public HospitalDto entityToDto(HospitalEntity entity) {
        if (entity == null) return null;
        return HospitalDto.builder()
            .id(entity.getId())
            .codiceRegione(entity.getCodiceRegione())
                .codiceAsl(entity.getCodiceAsl())
                .codiceStruttura(entity.getCodiceStruttura())
                .struttura(entity.getStruttura())
                .indirizzo(entity.getIndirizzo())
                .hospitalTypeId(entity.getHospitalType() != null ? entity.getHospitalType().getId() : null)
                .aslId(entity.getAsl() != null ? entity.getAsl().getId() : null)
                .build();
    }

    public HospitalEntity dtoToEntity(HospitalDto dto) {
        if (dto == null) return null;
        HospitalTypeEntity ht = null;
        if (dto.getHospitalTypeId() != null) ht = HospitalTypeEntity.builder().id(dto.getHospitalTypeId()).build();
        com.qtm.ticket.entity.ASLEntity asl = null;
        if (dto.getAslId() != null) asl = com.qtm.ticket.entity.ASLEntity.builder().id(dto.getAslId()).build();

        return HospitalEntity.builder()
            .id(dto.getId())
            .codiceRegione(dto.getCodiceRegione())
                .codiceAsl(dto.getCodiceAsl())
                .codiceStruttura(dto.getCodiceStruttura())
                .struttura(dto.getStruttura())
                .indirizzo(dto.getIndirizzo())
                .hospitalType(ht)
                .asl(asl)
                .build();
    }
}
