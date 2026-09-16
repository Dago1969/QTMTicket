package com.qtm.ticket.repository;

import com.qtm.ticket.entity.ProvinceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProvinceRepository extends JpaRepository<ProvinceEntity, Long> {
    List<ProvinceEntity> findByRegionId(Long regionId);
    List<ProvinceEntity> findAllByOrderByName();
    Optional<ProvinceEntity> findFirstBySiglaIgnoreCase(String sigla);
}
