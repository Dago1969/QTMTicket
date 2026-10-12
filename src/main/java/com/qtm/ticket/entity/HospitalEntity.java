package com.qtm.ticket.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "hospital", uniqueConstraints = {@UniqueConstraint(columnNames = {"codice_asl","codice_struttura"})})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HospitalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "anno")
    private Integer anno;

    @Column(name = "codice_regione", length = 10)
    private String codiceRegione;

    @Column(name = "regione", length = 200)
    private String regione;

    @Column(name = "codice_asl", length = 50)
    private String codiceAsl;

    @Column(name = "asl", length = 200)
    private String aslDescrizione;

    @Column(name = "codice_struttura", length = 50)
    private String codiceStruttura;

    @Column(name = "struttura", length = 500)
    private String struttura;

    @Column(name = "comune", length = 200)
    private String comune;

    @Column(name = "sigla_provincia", length = 20)
    private String siglaProvincia;

    @Column(name = "indirizzo", length = 500)
    private String indirizzo;

    @Column(name = "tipo_struttura", length = 200)
    private String tipoStruttura;

    @Column(name = "hospital_type_id")
    private Long hospitalTypeId;

    @Column(name = "asl_id")
    private Long aslId;

    @Column(name = "city_id")
    private Long cityId;
}
