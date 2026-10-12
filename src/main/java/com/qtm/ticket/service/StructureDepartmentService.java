package com.qtm.ticket.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.qtm.commonlib.dto.StructureDepartmentSourceDto;
import com.qtm.ticket.client.HealthStructureClient;
import com.qtm.ticket.dto.StructureDepartmentDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StructureDepartmentService {

    private final HealthStructureClient healthStructureClient;

    public List<StructureDepartmentDto> findAll() {
        return healthStructureClient.findDepartments(null).stream()
                .map(this::toDto)
                .toList();
    }

    public List<StructureDepartmentDto> findByStructure(String codiceStruttura) {
        return healthStructureClient.findDepartments(codiceStruttura).stream()
                .map(this::toDto)
                .toList();
    }

    public StructureDepartmentDto save(StructureDepartmentDto dto) {
        StructureDepartmentSourceDto sourceDto = StructureDepartmentSourceDto.builder()
                .id(dto.getId())
                .codiceStruttura(dto.getCodiceStruttura())
                .codiceDisciplina(dto.getCodiceDisciplina())
                .disciplina(dto.getDisciplina())
                .descrizioneDisciplina(dto.getDisciplina())
                .indirizzo(dto.getIndirizzo())
                .build();
        return toDto(healthStructureClient.createDepartment(sourceDto));
    }

    public boolean deleteByCodes(String codiceStruttura, String codiceDisciplina) {
        return healthStructureClient.findDepartments(codiceStruttura).stream()
                .filter(department -> codiceDisciplina.equals(department.getCodiceDisciplina()))
                .findFirst()
                .map(department -> {
                    healthStructureClient.deleteDepartment(department.getId());
                    return true;
                })
                .orElse(false);
    }

    private StructureDepartmentDto toDto(StructureDepartmentSourceDto sourceDto) {
        return StructureDepartmentDto.builder()
                .id(sourceDto.getId())
                .codiceStruttura(sourceDto.getCodiceStruttura())
                .codiceDisciplina(sourceDto.getCodiceDisciplina())
                .disciplina(sourceDto.getDisciplina())
                .indirizzo(sourceDto.getIndirizzo())
                .build();
    }
}
