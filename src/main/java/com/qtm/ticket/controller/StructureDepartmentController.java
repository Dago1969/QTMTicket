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

    @PostMapping
    public ResponseEntity<StructureDepartmentDto> create(@RequestBody StructureDepartmentDto dto) {
        return ResponseEntity.ok(structureDepartmentService.save(dto));
    }
}
