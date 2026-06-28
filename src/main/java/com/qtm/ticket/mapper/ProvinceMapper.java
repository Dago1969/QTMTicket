package com.qtm.ticket.mapper;

import org.springframework.stereotype.Component;

import com.qtm.ticket.dto.ProvinceDto;
import com.qtm.ticket.entity.ProvinceEntity;
import com.qtm.ticket.entity.RegionEntity;

/**
 * Mapper per ProvinceEntity e ProvinceDto.
 */
@Component
public class ProvinceMapper {

    public ProvinceDto entityToDto(ProvinceEntity entity) {
        if (entity == null) {
            return null;
        }
        return ProvinceDto.builder()
                .id(entity.getId())
                .regionId(entity.getRegion() != null ? entity.getRegion().getId() : null)
                .code(entity.getCode())
                .name(entity.getName())
                .sigla(entity.getSigla())
                .build();
    }

    public ProvinceEntity dtoToEntity(ProvinceDto dto) {
        if (dto == null) {
            return null;
        }
        RegionEntity region = null;
        if (dto.getRegionId() != null) {
            region = RegionEntity.builder().id(dto.getRegionId()).build();
        }
        return ProvinceEntity.builder()
                .id(dto.getId())
                .region(region)
                .code(dto.getCode())
                .name(dto.getName())
                .sigla(dto.getSigla())
                .build();
    }
}
