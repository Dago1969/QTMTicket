package com.qtm.ticket.entity;

import jakarta.persistence.*;
import lombok.*;

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

    @Column(name = "codice_regione", length = 10)
    private String codiceRegione;

    @Column(name = "codice_asl", length = 50)
    private String codiceAsl;

    @Column(name = "codice_struttura", length = 50)
    private String codiceStruttura;

    @Column(name = "struttura", length = 500)
    private String struttura;

    @Column(name = "indirizzo", length = 500)
    private String indirizzo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_type_id")
    private HospitalTypeEntity hospitalType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asl_id")
    private ASLEntity asl;
}
