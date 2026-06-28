package com.qtm.ticket.mapper;

import com.qtm.commonlib.dto.DepartmentDto;
import com.qtm.ticket.entity.DepartmentEntity;

public class DepartmentMapper {

    public static DepartmentDto entityToDto(DepartmentEntity e) {
        if (e == null) return null;
        return DepartmentDto.builder()
                .id(e.getId())
                .areaFunzionale(e.getAreaFunzionale())
                .reparto(e.getReparto())
                .mainResponsibilities(e.getMainResponsibilities())
                .exampleSymptoms(e.getExampleSymptoms())
                .emergenzaUrgenza(e.getEmergenzaUrgenza())
                .notes(e.getNotes())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .build();
    }

    public static DepartmentEntity dtoToEntity(DepartmentDto d) {
        if (d == null) return null;
        return DepartmentEntity.builder()
                .id(d.getId())
                .areaFunzionale(d.getAreaFunzionale())
                .reparto(d.getReparto())
                .mainResponsibilities(d.getMainResponsibilities())
                .exampleSymptoms(d.getExampleSymptoms())
                .emergenzaUrgenza(d.getEmergenzaUrgenza())
                .notes(d.getNotes())
                .build();
    }
}
