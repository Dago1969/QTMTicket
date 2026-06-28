package com.qtm.ticket.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.qtm.ticket.entity.StructureEntity;

public interface StructureRepository extends JpaRepository<StructureEntity, Long> {
    Optional<StructureEntity> findByCodiceRegioneAndCodiceAziendaAndCodiceStruttura(String codiceRegione, String codiceAzienda, String codiceStruttura);
    Optional<StructureEntity> findByCodiceStruttura(String codiceStruttura);
}
