package com.qtm.ticket.repository;

import com.qtm.ticket.entity.CityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CityRepository extends JpaRepository<CityEntity, Long> {
    List<CityEntity> findByProvinceId(Long provinceId);
    List<CityEntity> findAllByOrderByName();
}
