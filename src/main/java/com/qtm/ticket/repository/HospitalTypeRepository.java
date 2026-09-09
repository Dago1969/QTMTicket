package com.qtm.ticket.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.qtm.ticket.entity.HospitalTypeEntity;

public interface HospitalTypeRepository extends JpaRepository<HospitalTypeEntity, Long> {
}
