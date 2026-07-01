package com.qtm.ticket.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "hospital_type", uniqueConstraints = {@UniqueConstraint(columnNames = {"code"})})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HospitalTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", length = 100, nullable = false)
    private String code;

    @Column(name = "description", length = 500)
    private String description;
}
