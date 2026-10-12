package com.qtm.ticket.service;

import java.util.List;

import org.springframework.stereotype.Service;
import com.qtm.commonlib.dto.ASLDto;
import com.qtm.ticket.client.HealthStructureClient;

import lombok.RequiredArgsConstructor;

/**
 * Service per la gestione dei record ASL.
 */
@Service
@RequiredArgsConstructor
public class ASLService {

    private final HealthStructureClient healthStructureClient;

    public List<ASLDto> findAll() {
        return healthStructureClient.findAsl();
    }

    public ASLDto findById(Long id) {
        return healthStructureClient.findAsl(id).getBody();
    }

    public ASLDto save(ASLDto dto) {
        return healthStructureClient.createAsl(dto);
    }

    public void delete(Long id) {
        healthStructureClient.deleteAsl(id);
    }
}
