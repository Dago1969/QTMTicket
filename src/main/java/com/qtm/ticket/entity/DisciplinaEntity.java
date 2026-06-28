package com.qtm.ticket.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity che rappresenta una disciplina sanitaria importata dal CSV.
 */
@Entity
@Table(name = "discipline")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DisciplinaEntity {

    @Id
    @Column(name = "codice_disciplina", length = 20, nullable = false)
    private String codiceDisciplina;

    @Column(name = "disciplina", length = 200, nullable = false)
    private String disciplina;
}
