package com.qtm.ticket.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Tipo di ospedale mantenuto nella tabella legacy structure_types. */
@Entity
@Table(name = "structure_types")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HospitalTypeEntity {

    @Id
    @Column(name = "code", length = 50, nullable = false)
    private String code;

    @Column(name = "description", length = 500, nullable = false)
    private String description;
}