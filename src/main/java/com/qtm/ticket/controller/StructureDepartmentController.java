package com.qtm.ticket.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qtm.ticket.dto.StructureDepartmentDto;
import com.qtm.ticket.service.StructureDepartmentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/structure-departments")
@RequiredArgsConstructor
public class StructureDepartmentController {

    private final StructureDepartmentService structureDepartmentService;

    @GetMapping
    public List<StructureDepartmentDto> getAll() {
        return structureDepartmentService.findAll();
    }

    @GetMapping(params = "codiceStruttura")
    public List<StructureDepartmentDto> getByStructure(@org.springframework.web.bind.annotation.RequestParam String codiceStruttura) {
        return structureDepartmentService.findByStructure(codiceStruttura);
    }

    @PostMapping
    public ResponseEntity<StructureDepartmentDto> create(@RequestBody StructureDepartmentDto dto) {
        return ResponseEntity.ok(structureDepartmentService.save(dto));
    }

    @org.springframework.web.bind.annotation.DeleteMapping
    public ResponseEntity<Void> deleteByCodes(@org.springframework.web.bind.annotation.RequestParam String codiceStruttura,
            @org.springframework.web.bind.annotation.RequestParam String codiceDisciplina) {
        boolean deleted = structureDepartmentService.deleteByCodes(codiceStruttura, codiceDisciplina);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
