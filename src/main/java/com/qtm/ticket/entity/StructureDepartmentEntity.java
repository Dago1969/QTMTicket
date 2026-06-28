package com.qtm.ticket.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity che rappresenta la relazione tra una struttura e una disciplina.
 */
@Entity
@Table(name = "structure_departments", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"codice_struttura", "codice_disciplina"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StructureDepartmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codice_struttura", referencedColumnName = "codice_struttura")
    private StructureEntity structure;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codice_disciplina", referencedColumnName = "codice_disciplina")
    private DisciplinaEntity disciplina;

    @Column(name = "indirizzo", length = 500)
    private String indirizzo;
}
