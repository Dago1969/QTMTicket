package com.qtm.ticket.controller;

import com.qtm.ticket.service.ASLImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@RestController
@RequestMapping("/asl")
@RequiredArgsConstructor
public class ASLImportController {

    private final ASLImportService aslImportService;

    @PostMapping(path = "/import", consumes = {"multipart/form-data"})
    public ResponseEntity<?> importAsl(@RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body("file missing");
        }

        try {
            aslImportService.importCsv(file.getInputStream());
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("could not read file");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
