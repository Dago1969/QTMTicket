package com.qtm.ticket.repository;

import com.qtm.ticket.entity.ASLEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ASLRepository extends JpaRepository<ASLEntity, Long> {
	Optional<ASLEntity> findByCodiceRegioneAndCodiceAzienda(String codiceRegione, String codiceAzienda);
}
