package com.qtm.ticket.repository;

import com.qtm.ticket.entity.MedicineEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Accesso al catalogo farmaci condiviso persistito da QTMTicket.
 */
public interface MedicineRepository extends JpaRepository<MedicineEntity, Long> {

    boolean existsByCodiceAicIgnoreCase(String codiceAic);

    boolean existsByCodiceAicIgnoreCaseAndIdNot(String codiceAic, Long id);

    Optional<MedicineEntity> findByCodiceAicIgnoreCase(String codiceAic);

    @Query("""
            select medicine from MedicineEntity medicine
            where medicine.visibleForTenants = true
              and (upper(medicine.codiceAic) like upper(concat('%', :query, '%'))
                or upper(medicine.denominazione) like upper(concat('%', :query, '%'))
                or upper(coalesce(medicine.paAssociati, '')) like upper(concat('%', :query, '%'))
                or upper(coalesce(medicine.descrizione, '')) like upper(concat('%', :query, '%'))
                or upper(coalesce(medicine.forma, '')) like upper(concat('%', :query, '%')))
            order by medicine.denominazione asc, medicine.codiceAic asc
            """)
    List<MedicineEntity> lookupVisibleForTenants(@Param("query") String query, Pageable pageable);

        @Query("""
            select medicine from MedicineEntity medicine
            where (:codiceAic is null or :codiceAic = '' or upper(medicine.codiceAic) like upper(concat('%', :codiceAic, '%')))
              and (:codFarmaco is null or :codFarmaco = '' or upper(medicine.codFarmaco) like upper(concat('%', :codFarmaco, '%')))
              and (:codConfezione is null or :codConfezione = '' or upper(medicine.codConfezione) like upper(concat('%', :codConfezione, '%')))
              and (:denominazione is null or :denominazione = '' or upper(medicine.denominazione) like upper(concat('%', :denominazione, '%')))
              and (:descrizione is null or :descrizione = '' or upper(coalesce(medicine.descrizione, '')) like upper(concat('%', :descrizione, '%')))
              and (:codiceAtc is null or :codiceAtc = '' or upper(coalesce(medicine.codiceAtc, '')) like upper(concat('%', :codiceAtc, '%')))
              and (:ragioneSociale is null or :ragioneSociale = '' or upper(coalesce(medicine.ragioneSociale, '')) like upper(concat('%', :ragioneSociale, '%')))
              and (:statoAmministrativo is null or :statoAmministrativo = '' or upper(coalesce(medicine.statoAmministrativo, '')) like upper(concat('%', :statoAmministrativo, '%')))
            order by medicine.denominazione asc, medicine.codiceAic asc
            """)
        Page<MedicineEntity> searchAssociations(
            @Param("codiceAic") String codiceAic,
            @Param("codFarmaco") String codFarmaco,
            @Param("codConfezione") String codConfezione,
            @Param("denominazione") String denominazione,
            @Param("descrizione") String descrizione,
            @Param("codiceAtc") String codiceAtc,
            @Param("ragioneSociale") String ragioneSociale,
            @Param("statoAmministrativo") String statoAmministrativo,
            Pageable pageable
        );
}