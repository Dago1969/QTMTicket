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
 * Entity che rappresenta una struttura sanitaria importata dal file di elenco strutture.
 */
@Entity
@Table(name = "structures", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"codice_regione", "codice_azienda", "codice_struttura"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StructureEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codice_regione", length = 10, nullable = false)
    private String codiceRegione;

    @Column(name = "codice_azienda", length = 50, nullable = false)
    private String codiceAzienda;

    @Column(name = "codice_struttura", length = 50, nullable = false)
    private String codiceStruttura;

    @Column(name = "denominazione_struttura", length = 500, nullable = false)
    private String denominazioneStruttura;

    @Column(name = "indirizzo", length = 500)
    private String indirizzo;

    @Column(name = "codice_comune", length = 50)
    private String codiceComune;

    @Column(name = "comune", length = 200)
    private String comune;

    @Column(name = "sigla_provincia", length = 10)
    private String siglaProvincia;

    @Column(name = "city_id")
    private Long cityId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "structure_type_code")
    private StructureTypeEntity structureType;
}
