package com.qtm.ticket.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.qtm.ticket.entity.StructureDepartmentEntity;

public interface StructureDepartmentRepository extends JpaRepository<StructureDepartmentEntity, Long> {
}
