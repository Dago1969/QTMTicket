package com.qtm.ticket.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

/**
 * Entity che rappresenta una regione italiana.
 */
@Entity
@Table(name = "regions", uniqueConstraints = {@UniqueConstraint(columnNames = {"code"})})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", length = 10, nullable = false, unique = true)
    private String code;

    @Column(name = "name", length = 200, nullable = false)
    private String name;

    @Column(name = "geographic_area", length = 100)
    private String geographicArea;

    @Column(name = "region_type", length = 100)
    private String regionType;

    @OneToMany(mappedBy = "region", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProvinceEntity> provinces;
}
