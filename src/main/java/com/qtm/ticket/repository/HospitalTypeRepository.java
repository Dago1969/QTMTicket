package com.qtm.ticket.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.qtm.ticket.entity.HospitalTypeEntity;

import java.util.Optional;

public interface HospitalTypeRepository extends JpaRepository<HospitalTypeEntity, Long> {
	Optional<HospitalTypeEntity> findByCode(String code);
}
