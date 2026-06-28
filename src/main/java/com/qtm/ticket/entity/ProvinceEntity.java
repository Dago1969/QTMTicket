package com.qtm.ticket.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

/**
 * Entity che rappresenta una provincia italiana.
 */
@Entity
@Table(name = "provinces", uniqueConstraints = {@UniqueConstraint(columnNames = {"code"})})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProvinceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", length = 10, nullable = false, unique = true)
    private String code;

    @Column(name = "name", length = 200, nullable = false)
    private String name;

    @Column(name = "sigla", length = 10)
    private String sigla;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id", nullable = false)
    private RegionEntity region;

    @OneToMany(mappedBy = "province", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CityEntity> cities;
}
