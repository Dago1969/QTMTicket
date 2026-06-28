package com.qtm.ticket.controller;

import com.qtm.commonlib.dto.ASLDto;
import com.qtm.ticket.service.ASLService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller REST per esporre le ASL presenti in QTMTicket.
 */
@RestController
@RequestMapping("/asl")
@RequiredArgsConstructor
public class ASLController {

    private final ASLService aslService;

    @GetMapping
    public ResponseEntity<List<ASLDto>> findAll() {
        return ResponseEntity.ok(aslService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ASLDto> findById(@PathVariable Long id) {
        ASLDto dto = aslService.findById(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }
}
