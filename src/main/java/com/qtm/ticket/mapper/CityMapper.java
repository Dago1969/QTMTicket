package com.qtm.ticket.mapper;

import org.springframework.stereotype.Component;

import com.qtm.ticket.dto.CityDto;
import com.qtm.ticket.entity.CityEntity;
import com.qtm.ticket.entity.ProvinceEntity;

/**
 * Mapper per CityEntity e CityDto.
 */
@Component
public class CityMapper {

    public CityDto entityToDto(CityEntity entity) {
        if (entity == null) {
            return null;
        }
        return CityDto.builder()
                .id(entity.getId())
                .provinceId(entity.getProvince() != null ? entity.getProvince().getId() : null)
                .code(entity.getCode())
                .istatCode(entity.getIstatCode())
                .catastaleCode(entity.getCatastaleCode())
                .name(entity.getName())
                .capoluogo(entity.getCapoluogo())
                .build();
    }

    public CityEntity dtoToEntity(CityDto dto) {
        if (dto == null) {
            return null;
        }
        ProvinceEntity province = null;
        if (dto.getProvinceId() != null) {
            province = ProvinceEntity.builder().id(dto.getProvinceId()).build();
        }
        return CityEntity.builder()
                .id(dto.getId())
                .province(province)
                .code(dto.getCode())
                .istatCode(dto.getIstatCode())
                .catastaleCode(dto.getCatastaleCode())
                .name(dto.getName())
                .capoluogo(dto.getCapoluogo())
                .build();
    }
}
