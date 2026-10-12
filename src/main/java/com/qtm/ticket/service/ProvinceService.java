package com.qtm.ticket.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.qtm.commonlib.dto.ProvinceDto;
import com.qtm.ticket.client.QtmGeographyClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service per la gestione delle province.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProvinceService {

    private final QtmGeographyClient geographyClient;

    public List<ProvinceDto> findAll() {
        return geographyClient.findProvinces();
    }

    public List<ProvinceDto> findByRegionId(Long regionId) {
        return geographyClient.findProvincesByRegionId(regionId);
    }

    public ProvinceDto findById(Long id) {
        return geographyClient.findProvinceById(id).getBody();
    }

    public ProvinceDto save(ProvinceDto dto) {
        return dto.getId() == null ? geographyClient.createProvince(dto) : geographyClient.updateProvince(dto.getId(), dto);
    }

    public void delete(Long id) {
        geographyClient.deleteProvince(id);
    }
}
