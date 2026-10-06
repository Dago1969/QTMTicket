package com.qtm.ticket.service;

import java.util.List;

import feign.FeignException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.qtm.ticket.client.QtmGeographyClient;
import com.qtm.commonlib.dto.RegionDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static org.springframework.http.HttpStatus.BAD_GATEWAY;

/**
 * Service per la gestione delle regioni.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RegionService {

    private final QtmGeographyClient geographyClient;

    public List<RegionDto> findAll() {
        return geographyClient.findRegions();
    }

    public RegionDto findById(Long id) {
        try {
            return geographyClient.findRegionById(id).getBody();
        } catch (FeignException exception) {
            log.error("[RegionService] Geography service unavailable for regionId={}", id, exception);
            throw new ResponseStatusException(BAD_GATEWAY, "Servizio geografia non disponibile", exception);
        }
    }

    public RegionDto save(RegionDto dto) {
        try {
            return dto.getId() == null ? geographyClient.createRegion(dto) : geographyClient.updateRegion(dto.getId(), dto);
        } catch (FeignException exception) {
            throw new ResponseStatusException(BAD_GATEWAY, "Servizio geografia non disponibile", exception);
        }
    }

    public void delete(Long id) {
        try {
            geographyClient.deleteRegion(id);
        } catch (FeignException exception) {
            throw new ResponseStatusException(BAD_GATEWAY, "Servizio geografia non disponibile", exception);
        }
    }
}
