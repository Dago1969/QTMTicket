package com.qtm.ticket.service;

import com.qtm.commonlib.dto.FarmacoDto;
import com.qtm.commonlib.dto.MedicineDto;
import com.qtm.ticket.entity.MedicineEntity;
import com.qtm.ticket.mapper.MedicineMapper;
import com.qtm.ticket.repository.MedicineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * Gestisce ricerca, CRUD e associazioni del catalogo farmaci condiviso in QTMTicket.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class MedicineService {

    private static final int LOOKUP_LIMIT = 30;
    private static final int TENANT_LOOKUP_LIMIT = 50;
    private static final int ASSOCIATION_PAGE_SIZE_LIMIT = 100;

    private final MedicineRepository medicineRepository;
    private final MedicineMapper medicineMapper;

    public MedicineDto create(MedicineDto medicineDto) {
        validateUniqueCodiceAicForCreate(medicineDto.getCodiceAic());
        return medicineMapper.toDto(medicineRepository.save(medicineMapper.toEntity(medicineDto)));
    }

    @Transactional(readOnly = true)
    public List<MedicineDto> search(MedicineSearchCriteria criteria) {
        return medicineRepository.findAll().stream()
                .filter(medicine -> matches(medicine, criteria))
                .map(medicineMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public MedicineDto findById(Long id) {
        return medicineMapper.toDto(findEntityById(id));
    }

    @Transactional(readOnly = true)
    public MedicineDto findByCodiceAic(String codiceAic) {
        String normalized = normalizeRequiredValue(codiceAic);
        return medicineRepository.findByCodiceAicIgnoreCase(normalized)
                .map(medicineMapper::toDto)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Farmaco non trovato"));
    }

    @Transactional(readOnly = true)
    public List<MedicineDto> lookup(String query) {
        return medicineRepository.lookupVisibleForTenants(normalizeQuery(query), PageRequest.of(0, LOOKUP_LIMIT)).stream()
                .map(medicineMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<FarmacoDto> searchVisibleForTenants(String query) {
        String normalizedQuery = normalizeQuery(query);
        if (normalizedQuery.length() < 3) {
            return List.of();
        }
        return medicineRepository.lookupVisibleForTenants(normalizedQuery, PageRequest.of(0, TENANT_LOOKUP_LIMIT)).stream()
                .map(this::toFarmacoDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public Page<MedicineDto> searchAssociations(MedicineSearchCriteria criteria, int page, int size) {
        int pageSize = Math.min(Math.max(size, 1), ASSOCIATION_PAGE_SIZE_LIMIT);
        return medicineRepository.searchAssociations(
                        normalizeQuery(criteria.codiceAic()),
                        normalizeQuery(criteria.codFarmaco()),
                        normalizeQuery(criteria.codConfezione()),
                        normalizeQuery(criteria.denominazione()),
                        normalizeQuery(criteria.descrizione()),
                        normalizeQuery(criteria.codiceAtc()),
                        normalizeQuery(criteria.ragioneSociale()),
                        normalizeQuery(criteria.statoAmministrativo()),
                        PageRequest.of(Math.max(page, 0), pageSize, Sort.by("denominazione").ascending()))
                .map(medicineMapper::toDto);
    }

    public MedicineDto updateTenantVisibility(Long id, boolean visible) {
        MedicineEntity medicine = findEntityById(id);
        medicine.setVisibleForTenants(visible);
        return medicineMapper.toDto(medicineRepository.save(medicine));
    }

    public MedicineDto update(Long id, MedicineDto medicineDto) {
        MedicineEntity current = findEntityById(id);
        if (medicineRepository.existsByCodiceAicIgnoreCaseAndIdNot(medicineDto.getCodiceAic(), id)) {
            throw new ResponseStatusException(CONFLICT, "Esiste gia un farmaco con lo stesso codice AIC");
        }
        medicineMapper.updateEntity(current, medicineDto);
        return medicineMapper.toDto(medicineRepository.save(current));
    }

    public void delete(Long id) {
        medicineRepository.delete(findEntityById(id));
    }

    private MedicineEntity findEntityById(Long id) {
        return medicineRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Farmaco non trovato"));
    }

    private void validateUniqueCodiceAicForCreate(String codiceAic) {
        if (medicineRepository.existsByCodiceAicIgnoreCase(codiceAic)) {
            throw new ResponseStatusException(CONFLICT, "Esiste gia un farmaco con lo stesso codice AIC");
        }
    }

    private boolean matches(MedicineEntity medicine, MedicineSearchCriteria criteria) {
        return contains(medicine.getCodiceAic(), criteria.codiceAic())
                && contains(medicine.getCodFarmaco(), criteria.codFarmaco())
                && contains(medicine.getCodConfezione(), criteria.codConfezione())
                && contains(medicine.getDenominazione(), criteria.denominazione())
                && contains(medicine.getDescrizione(), criteria.descrizione())
                && contains(medicine.getCodiceAtc(), criteria.codiceAtc())
                && contains(medicine.getRagioneSociale(), criteria.ragioneSociale())
                && contains(medicine.getStatoAmministrativo(), criteria.statoAmministrativo());
    }

    private boolean contains(String actual, String expected) {
        return expected == null || expected.isBlank()
                || actual != null && actual.toLowerCase().contains(expected.trim().toLowerCase());
    }

    private String normalizeQuery(String query) {
        return query == null ? "" : query.trim();
    }

    private String normalizeRequiredValue(String value) {
        String normalized = normalizeQuery(value);
        if (normalized.isEmpty()) {
            throw new ResponseStatusException(NOT_FOUND, "Farmaco non trovato");
        }
        return normalized;
    }

    private FarmacoDto toFarmacoDto(MedicineEntity medicine) {
        FarmacoDto dto = new FarmacoDto();
        dto.setAicCode(medicine.getCodiceAic());
        dto.setTradeName(medicine.getDenominazione());
        dto.setActiveIngredient(medicine.getPaAssociati());
        dto.setDosage(joinDosage(medicine.getDescrizione(), medicine.getForma()));
        return dto;
    }

    private String joinDosage(String description, String form) {
        if (description == null || description.isBlank()) {
            return form == null ? "" : form;
        }
        if (form == null || form.isBlank() || description.equalsIgnoreCase(form)) {
            return description;
        }
        return description + " - " + form;
    }
}