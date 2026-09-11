package com.qtm.ticket.controller;

import java.util.List;
import java.io.IOException;

import com.qtm.commonlib.dto.HospitalDto;
import com.qtm.ticket.service.HospitalImportService;
import com.qtm.ticket.service.HospitalService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/hospitals")
@RequiredArgsConstructor
public class HospitalController {

    private final HospitalService hospitalService;
    private final HospitalImportService hospitalImportService;

    @GetMapping
    public ResponseEntity<List<HospitalDto>> findAll() {
        return ResponseEntity.ok(hospitalService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HospitalDto> findById(@PathVariable Long id) {
        var dto = hospitalService.findById(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @PostMapping(path = "/import", consumes = {"multipart/form-data"})
    public ResponseEntity<?> importHospitals(@RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body("file missing");
        }

        try {
            hospitalImportService.importCsv(file.getInputStream());
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IOException exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("could not read file");
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
        }
    }
}
