package com.qtm.ticket.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.qtm.ticket.entity.HospitalCatalogEntity;

public interface HospitalCatalogRepository extends JpaRepository<HospitalCatalogEntity, Long> {
    Optional<HospitalCatalogEntity> findByCodiceRegioneAndCodiceAziendaAndCodiceStruttura(String codiceRegione, String codiceAzienda, String codiceStruttura);
    Optional<HospitalCatalogEntity> findByCodiceStruttura(String codiceStruttura);
    Optional<HospitalCatalogEntity> findByCodiceStrutturaAndActiveTrue(String codiceStruttura);
    java.util.List<HospitalCatalogEntity> findAllByActiveTrue();
}