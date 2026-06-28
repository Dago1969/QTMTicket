package com.qtm.ticket.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entity che rappresenta una città italiana.
 */
@Entity
@Table(name = "cities", uniqueConstraints = {@UniqueConstraint(columnNames = {"code"})})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", length = 20, nullable = false, unique = true)
    private String code;

    @Column(name = "istat_code", length = 20)
    private String istatCode;

    @Column(name = "catastale_code", length = 20)
    private String catastaleCode;

    @Column(name = "name", length = 200, nullable = false)
    private String name;

    @Column(name = "is_capoluogo")
    private Boolean capoluogo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "province_id", nullable = false)
    private ProvinceEntity province;
}
