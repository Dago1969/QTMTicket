package com.qtm.ticket.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.qtm.ticket.entity.StructureDepartmentEntity;

public interface StructureDepartmentRepository extends JpaRepository<StructureDepartmentEntity, Long> {
    List<StructureDepartmentEntity> findByStructure_CodiceStruttura(String codiceStruttura);

    Optional<StructureDepartmentEntity> findByStructure_CodiceStrutturaAndDisciplina_CodiceDisciplina(
	    String codiceStruttura, String codiceDisciplina);

    void deleteByStructure_CodiceStrutturaAndDisciplina_CodiceDisciplina(String codiceStruttura,
	    String codiceDisciplina);
}
