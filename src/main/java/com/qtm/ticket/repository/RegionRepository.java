package com.qtm.ticket.repository;

import com.qtm.ticket.entity.RegionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RegionRepository extends JpaRepository<RegionEntity, Long> {
    List<RegionEntity> findAllByOrderByName();
    Optional<RegionEntity> findByCode(String code);
    Optional<RegionEntity> findFirstByNameIgnoreCase(String name);
}
