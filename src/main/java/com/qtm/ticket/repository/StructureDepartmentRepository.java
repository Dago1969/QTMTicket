package com.qtm.ticket.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.qtm.ticket.entity.StructureDepartmentEntity;

public interface StructureDepartmentRepository extends JpaRepository<StructureDepartmentEntity, Long> {
    List<StructureDepartmentEntity> findByCodiceStruttura(String codiceStruttura);

    Optional<StructureDepartmentEntity> findByCodiceStrutturaAndDisciplina_CodiceDisciplina(
	    String codiceStruttura, String codiceDisciplina);

    void deleteByCodiceStrutturaAndDisciplina_CodiceDisciplina(String codiceStruttura,
	    String codiceDisciplina);
}
