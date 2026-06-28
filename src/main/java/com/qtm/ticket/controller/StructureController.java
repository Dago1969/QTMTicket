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

import com.qtm.ticket.dto.StructureDto;
import com.qtm.ticket.service.StructureService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * REST Controller per la gestione delle strutture sanitarie.
 */
@RestController
@RequestMapping("/api/structures")
@RequiredArgsConstructor
@Slf4j
public class StructureController {

    private final StructureService structureService;

    @GetMapping
    public List<StructureDto> getAll() {
        log.info("Recupero tutte le strutture");
        return structureService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<StructureDto> getById(@PathVariable Long id) {
        StructureDto dto = structureService.findById(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<StructureDto> create(@RequestBody StructureDto dto) {
        log.info("Creazione struttura: {}", dto.getDenominazioneStruttura());
        return ResponseEntity.ok(structureService.save(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StructureDto> update(@PathVariable Long id, @RequestBody StructureDto dto) {
        dto.setId(id);
        return ResponseEntity.ok(structureService.save(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        structureService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
