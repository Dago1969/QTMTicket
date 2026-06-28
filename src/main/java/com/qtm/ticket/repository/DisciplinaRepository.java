package com.qtm.ticket.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.qtm.ticket.entity.DisciplinaEntity;

public interface DisciplinaRepository extends JpaRepository<DisciplinaEntity, String> {
}
