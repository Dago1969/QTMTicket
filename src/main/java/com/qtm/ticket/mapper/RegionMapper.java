package com.qtm.ticket.mapper;

import org.springframework.stereotype.Component;

import com.qtm.ticket.dto.RegionDto;
import com.qtm.ticket.entity.RegionEntity;

/**
 * Mapper per RegionEntity e RegionDto.
 */
@Component
public class RegionMapper {

    public RegionDto entityToDto(RegionEntity entity) {
        if (entity == null) {
            return null;
        }
        return RegionDto.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .name(entity.getName())
                .geographicArea(entity.getGeographicArea())
                .regionType(entity.getRegionType())
                .build();
    }

    public RegionEntity dtoToEntity(RegionDto dto) {
        if (dto == null) {
            return null;
        }
        return RegionEntity.builder()
                .id(dto.getId())
                .code(dto.getCode())
                .name(dto.getName())
                .geographicArea(dto.getGeographicArea())
                .regionType(dto.getRegionType())
                .build();
    }
}
