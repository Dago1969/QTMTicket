package com.qtm.ticket.drug.controller;

import com.qtm.commonlib.dto.FarmacoDto;
import com.qtm.ticket.drug.service.FarmadatiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Endpoint comune TICKET per cercare i farmaci nel catalogo AIFA.
 */
@RestController
@RequestMapping("/api/tenants/ticket/drugs")
@RequiredArgsConstructor
public class FarmadatiController {

    private final FarmadatiService farmadatiService;

    @GetMapping("/search")
    public ResponseEntity<List<FarmacoDto>> search(@RequestParam String query) {
        return ResponseEntity.ok(farmadatiService.search(query));
    }
}