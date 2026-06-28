package com.qtm.ticket.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entity che rappresenta i record ASL importati da ASL_2010-2026.csv.
 */
@Entity
@Table(name = "asl", uniqueConstraints = {@UniqueConstraint(columnNames = {"codice_azienda", "denominazione_azienda"})})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ASLEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codice_azienda", length = 50, nullable = false)
    private String codiceAzienda;

    @Column(name = "denominazione_azienda", length = 500, nullable = false)
    private String denominazioneAzienda;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id")
    private CityEntity city;

    @Column(name = "codice_regione", length = 10, nullable = false)
    private String codiceRegione;

    @Column(name = "indirizzo", length = 500)
    private String indirizzo;

    @Column(name = "cap", length = 20)
    private String cap;

    @Column(name = "telefono", length = 100)
    private String telefono;

    @Column(name = "fax", length = 100)
    private String fax;

    @Column(name = "email", length = 200)
    private String email;

    @Column(name = "sito_web", length = 200)
    private String sitoWeb;

    @Column(name = "partita_iva", length = 50)
    private String partitaIva;
}
