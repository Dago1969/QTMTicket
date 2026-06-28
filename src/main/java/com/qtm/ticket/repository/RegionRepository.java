package com.qtm.ticket.repository;

import com.qtm.ticket.entity.RegionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegionRepository extends JpaRepository<RegionEntity, Long> {
    List<RegionEntity> findAllByOrderByName();
}
