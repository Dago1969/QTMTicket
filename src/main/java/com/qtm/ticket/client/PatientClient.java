package com.qtm.ticket.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import com.qtm.commonlib.dto.PatientDto;

/** Recupera i dati paziente da QTMDB senza introdurre anagrafiche locali in TICKET. */
@FeignClient(name = "qtmdb-patient-client", url = "${qtmdb.feign.url:http://localhost:8080}", configuration = QtmdbFeignConfiguration.class)
public interface PatientClient {

    @GetMapping("/api/patients/{patientId}")
    PatientDto getPatientById(
            @PathVariable("patientId") Long patientId,
            @RequestHeader("X-QTM-Realm") String realm,
            @RequestHeader("X-QTM-Project") String project);
}
