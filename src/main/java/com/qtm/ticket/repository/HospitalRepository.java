package com.qtm.ticket.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.qtm.ticket.entity.HospitalEntity;

public interface HospitalRepository extends JpaRepository<HospitalEntity, Long> {
}
