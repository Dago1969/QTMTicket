package com.qtm.ticket.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.qtm.ticket.dto.HospitalCatalogDto;
import com.qtm.ticket.entity.HospitalCatalogEntity;
import com.qtm.ticket.entity.HospitalTypeEntity;
import com.qtm.ticket.mapper.HospitalCatalogMapper;
import com.qtm.ticket.repository.HospitalCatalogRepository;
import com.qtm.ticket.repository.HospitalTypeRepository;

import lombok.RequiredArgsConstructor;

/** Service per la gestione della cache locale degli ospedali. */
@Service
@RequiredArgsConstructor
public class HospitalCatalogService {

    private final HospitalCatalogRepository hospitalCatalogRepository;
    private final HospitalCatalogMapper hospitalCatalogMapper;
    private final HospitalTypeRepository hospitalTypeRepository;

    public List<HospitalCatalogDto> findAll() {
        return hospitalCatalogRepository.findAllByActiveTrue().stream()
                .map(hospitalCatalogMapper::entityToDto)
                .toList();
    }

    public HospitalCatalogDto findById(Long id) {
        return hospitalCatalogRepository.findById(id)
                .map(hospitalCatalogMapper::entityToDto)
                .orElse(null);
    }

    public HospitalCatalogDto save(HospitalCatalogDto dto) {
        HospitalCatalogEntity entity = hospitalCatalogMapper.dtoToEntity(dto);
        return hospitalCatalogMapper.entityToDto(hospitalCatalogRepository.save(entity));
    }

    public HospitalCatalogDto upsertByKeys(HospitalCatalogDto dto) {
        String codiceRegione = dto.getCodiceRegione();
        String codiceAzienda = dto.getCodiceAzienda();
        String codiceStruttura = dto.getCodiceStruttura();
        HospitalCatalogEntity entity = hospitalCatalogRepository.findByCodiceRegioneAndCodiceAziendaAndCodiceStruttura(
                        codiceRegione, codiceAzienda, codiceStruttura)
                .orElse(HospitalCatalogEntity.builder()
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
            HospitalTypeEntity type = hospitalTypeRepository.findById(dto.getStructureTypeCode())
                    .orElse(HospitalTypeEntity.builder().code(dto.getStructureTypeCode())
                            .description(dto.getStructureTypeDescription()).build());
            entity.setHospitalType(type);
        }
        if (entity.getActive() == null) {
            entity.setActive(dto.getActive() != null ? dto.getActive() : false);
        }
        return hospitalCatalogMapper.entityToDto(hospitalCatalogRepository.save(entity));
    }

    public void delete(Long id) {
        hospitalCatalogRepository.deleteById(id);
    }
}