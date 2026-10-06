package com.qtm.ticket.service;

import java.util.List;

import org.springframework.stereotype.Service;
import com.qtm.commonlib.dto.HospitalDto;
import com.qtm.ticket.client.HealthStructureClient;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HospitalService {

    private final HealthStructureClient healthStructureClient;

    public List<HospitalDto> findAll() {
        return healthStructureClient.findHospitals();
    }

    public HospitalDto findById(Long id) {
        return healthStructureClient.findHospital(id).getBody();
    }

    public HospitalDto save(HospitalDto dto) {
        return healthStructureClient.createHospital(dto);
    }

    public void delete(Long id) {
        healthStructureClient.deleteHospital(id);
    }
}
