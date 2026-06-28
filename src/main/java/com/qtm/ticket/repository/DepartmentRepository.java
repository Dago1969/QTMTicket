package com.qtm.ticket.repository;

import com.qtm.ticket.entity.DepartmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<DepartmentEntity, Long> {
    List<DepartmentEntity> findByAreaFunzionale(String areaFunzionale);
}
