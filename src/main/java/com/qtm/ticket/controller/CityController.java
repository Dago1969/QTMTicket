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

import com.qtm.ticket.dto.CityDto;
import com.qtm.ticket.service.CityService;

import lombok.RequiredArgsConstructor;

/**
 * REST Controller per la gestione delle città.
 */
@RestController
@RequestMapping("/api/cities")
@RequiredArgsConstructor
public class CityController {

    private final CityService cityService;

    @GetMapping
    public List<CityDto> getAll() {
        return cityService.findAll();
    }

    @GetMapping("/by-province/{provinceId}")
    public List<CityDto> getByProvince(@PathVariable Long provinceId) {
        return cityService.findByProvinceId(provinceId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CityDto> getById(@PathVariable Long id) {
        CityDto dto = cityService.findById(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public CityDto create(@RequestBody CityDto dto) {
        return cityService.save(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CityDto> update(@PathVariable Long id, @RequestBody CityDto dto) {
        dto.setId(id);
        return ResponseEntity.ok(cityService.save(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        cityService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
