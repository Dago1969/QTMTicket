package com.qtm.ticket.service;

import com.qtm.commonlib.dto.DepartmentDto;
import com.qtm.ticket.entity.DepartmentEntity;
import com.qtm.ticket.mapper.DepartmentMapper;
import com.qtm.ticket.repository.DepartmentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class DepartmentService {

    private final DepartmentRepository repository;

    public DepartmentService(DepartmentRepository repository) {
        this.repository = repository;
    }

    public DepartmentDto createDepartment(DepartmentDto dto) {
        log.debug("Creazione reparto: {} - {}", dto.getAreaFunzionale(), dto.getReparto());
        DepartmentEntity entity = DepartmentMapper.dtoToEntity(dto);
        DepartmentEntity saved = repository.save(entity);
        return DepartmentMapper.entityToDto(saved);
    }

    public DepartmentDto getById(Long id) {
        DepartmentEntity e = repository.findById(id).orElseThrow(() -> new RuntimeException("Department not found: " + id));
        return DepartmentMapper.entityToDto(e);
    }

    public List<DepartmentDto> findAll() {
        return repository.findAll().stream().map(DepartmentMapper::entityToDto).collect(Collectors.toList());
    }

    public List<DepartmentDto> findByArea(String area) {
        return repository.findByAreaFunzionale(area).stream().map(DepartmentMapper::entityToDto).collect(Collectors.toList());
    }

    public DepartmentDto update(Long id, DepartmentDto dto) {
        DepartmentEntity e = repository.findById(id).orElseThrow(() -> new RuntimeException("Department not found: " + id));
        e.setAreaFunzionale(dto.getAreaFunzionale());
        e.setReparto(dto.getReparto());
        e.setMainResponsibilities(dto.getMainResponsibilities());
        e.setExampleSymptoms(dto.getExampleSymptoms());
        e.setEmergenzaUrgenza(dto.getEmergenzaUrgenza());
        e.setNotes(dto.getNotes());
        DepartmentEntity updated = repository.save(e);
        return DepartmentMapper.entityToDto(updated);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) throw new RuntimeException("Department not found: " + id);
        repository.deleteById(id);
    }
}
