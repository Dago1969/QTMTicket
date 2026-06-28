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

import com.qtm.ticket.dto.RegionDto;
import com.qtm.ticket.service.RegionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * REST Controller per la gestione delle regioni.
 */
@RestController
@RequestMapping("/api/regions")
@RequiredArgsConstructor
@Slf4j
public class RegionController {

    private final RegionService regionService;

    @GetMapping
    public List<RegionDto> getAll() {
        log.info("Recupero tutte le regioni");
        return regionService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegionDto> getById(@PathVariable Long id) {
        RegionDto dto = regionService.findById(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<RegionDto> create(@RequestBody RegionDto dto) {
        log.info("Creazione regione: {}", dto.getName());
        return ResponseEntity.ok(regionService.save(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RegionDto> update(@PathVariable Long id, @RequestBody RegionDto dto) {
        dto.setId(id);
        return ResponseEntity.ok(regionService.save(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        regionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
