package com.qtm.ticket.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.qtm.ticket.entity.ASLEntity;

public interface ASLRepository extends JpaRepository<ASLEntity, Long> {
}
