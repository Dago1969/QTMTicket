package com.qtm.ticket.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.qtm.ticket.dto.StructureDto;
import com.qtm.ticket.entity.StructureEntity;
import com.qtm.ticket.entity.StructureTypeEntity;
import com.qtm.ticket.mapper.StructureMapper;
import com.qtm.ticket.repository.StructureRepository;
import com.qtm.ticket.repository.StructureTypeRepository;

import lombok.RequiredArgsConstructor;

/**
 * Service per la gestione delle strutture sanitarie.
 */
@Service
@RequiredArgsConstructor
public class StructureService {

    private final StructureRepository structureRepository;
    private final StructureMapper structureMapper;
    private final StructureTypeRepository structureTypeRepository;

    public List<StructureDto> findAll() {
        return structureRepository.findAll().stream()
                .map(structureMapper::entityToDto)
                .toList();
    }

    public StructureDto findById(Long id) {
        return structureRepository.findById(id)
                .map(structureMapper::entityToDto)
                .orElse(null);
    }

    public StructureDto save(StructureDto dto) {
        StructureEntity entity = structureMapper.dtoToEntity(dto);
        return structureMapper.entityToDto(structureRepository.save(entity));
    }

    public StructureDto upsertByKeys(StructureDto dto) {
        String codiceRegione = dto.getCodiceRegione();
        String codiceAzienda = dto.getCodiceAzienda();
        String codiceStruttura = dto.getCodiceStruttura();
        StructureEntity entity = structureRepository.findByCodiceRegioneAndCodiceAziendaAndCodiceStruttura(
                        codiceRegione, codiceAzienda, codiceStruttura)
                .orElse(StructureEntity.builder()
                        .codiceRegione(codiceRegione)
                        .codiceAzienda(codiceAzienda)
                        .codiceStruttura(codiceStruttura)
                        .build());
        entity.setDenominazioneStruttura(dto.getDenominazioneStruttura());
        entity.setIndirizzo(dto.getIndirizzo());
        entity.setCodiceComune(dto.getCodiceComune());
        entity.setComune(dto.getComune());
        entity.setSiglaProvincia(dto.getSiglaProvincia());
        entity.setCityId(dto.getCityId());
        if (dto.getStructureTypeCode() != null) {
            StructureTypeEntity type = structureTypeRepository.findById(dto.getStructureTypeCode())
                    .orElse(StructureTypeEntity.builder().code(dto.getStructureTypeCode()).description(dto.getStructureTypeDescription()).build());
            entity.setStructureType(type);
        }
        return structureMapper.entityToDto(structureRepository.save(entity));
    }

    public void delete(Long id) {
        structureRepository.deleteById(id);
    }
}
