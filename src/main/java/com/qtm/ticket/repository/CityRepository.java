package com.qtm.ticket.repository;

import com.qtm.ticket.entity.CityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CityRepository extends JpaRepository<CityEntity, Long> {
    List<CityEntity> findByProvinceId(Long provinceId);
    List<CityEntity> findAllByOrderByName();
    Optional<CityEntity> findByCode(String code);
    Optional<CityEntity> findByIstatCode(String istatCode);
    Optional<CityEntity> findByCatastaleCode(String catastaleCode);
    Optional<CityEntity> findFirstByNameIgnoreCaseAndProvince_SiglaIgnoreCase(String name, String sigla);
}
