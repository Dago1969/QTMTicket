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

import com.qtm.ticket.dto.ProvinceDto;
import com.qtm.ticket.service.ProvinceService;

import lombok.RequiredArgsConstructor;

/**
 * REST Controller per la gestione delle province.
 */
@RestController
@RequestMapping("/api/provinces")
@RequiredArgsConstructor
public class ProvinceController {

    private final ProvinceService provinceService;

    @GetMapping
    public List<ProvinceDto> getAll() {
        return provinceService.findAll();
    }

    @GetMapping("/by-region/{regionId}")
    public List<ProvinceDto> getByRegion(@PathVariable Long regionId) {
        return provinceService.findByRegionId(regionId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProvinceDto> getById(@PathVariable Long id) {
        ProvinceDto dto = provinceService.findById(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ProvinceDto create(@RequestBody ProvinceDto dto) {
        return provinceService.save(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProvinceDto> update(@PathVariable Long id, @RequestBody ProvinceDto dto) {
        dto.setId(id);
        return ResponseEntity.ok(provinceService.save(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        provinceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
