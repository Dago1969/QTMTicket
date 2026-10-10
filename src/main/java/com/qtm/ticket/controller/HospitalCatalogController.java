package com.qtm.ticket.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qtm.ticket.dto.HospitalCatalogDto;
import com.qtm.ticket.service.HospitalCatalogService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/** API della cache locale ospedali, mantenendo i route structures per compatibilità. */
@RestController
@RequestMapping({"/api/hospital-catalog", "/hospital-catalog", "/api/structures", "/structures"})
@RequiredArgsConstructor
@Slf4j
public class HospitalCatalogController {

    private final HospitalCatalogService hospitalCatalogService;

    @GetMapping
    public List<HospitalCatalogDto> getAll() {
        log.info("Recupero tutti gli ospedali dal catalogo locale");
        return hospitalCatalogService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<HospitalCatalogDto> getById(@PathVariable Long id) {
        HospitalCatalogDto dto = hospitalCatalogService.findById(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<HospitalCatalogDto> create(@RequestBody HospitalCatalogDto dto) {
        log.info("Creazione ospedale catalogo locale: {}", dto.getDenominazioneStruttura());
        return ResponseEntity.ok(hospitalCatalogService.save(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HospitalCatalogDto> update(@PathVariable Long id, @RequestBody HospitalCatalogDto dto) {
        dto.setId(id);
        return ResponseEntity.ok(hospitalCatalogService.save(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        hospitalCatalogService.delete(id);
        return ResponseEntity.noContent().build();
    }
}