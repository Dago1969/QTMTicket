package com.qtm.ticket.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.qtm.ticket.entity.HospitalEntity;

import java.util.Optional;

public interface HospitalRepository extends JpaRepository<HospitalEntity, Long> {
	Optional<HospitalEntity> findByCodiceAslAndCodiceStruttura(String codiceAsl, String codiceStruttura);
}
