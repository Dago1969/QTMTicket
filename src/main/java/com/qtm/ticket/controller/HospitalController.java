package com.qtm.ticket.controller;

import java.util.List;

import com.qtm.commonlib.dto.HospitalDto;
import com.qtm.ticket.service.HospitalService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hospitals")
@RequiredArgsConstructor
public class HospitalController {

    private final HospitalService hospitalService;

    @GetMapping
    public ResponseEntity<List<HospitalDto>> findAll() {
        return ResponseEntity.ok(hospitalService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HospitalDto> findById(@PathVariable Long id) {
        var dto = hospitalService.findById(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }
}
