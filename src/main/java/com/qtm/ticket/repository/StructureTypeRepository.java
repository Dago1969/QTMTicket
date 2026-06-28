package com.qtm.ticket.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.qtm.ticket.entity.StructureTypeEntity;

public interface StructureTypeRepository extends JpaRepository<StructureTypeEntity, String> {
}
