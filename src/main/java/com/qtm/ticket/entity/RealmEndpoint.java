package com.qtm.ticket.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "realm_endpoint")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RealmEndpoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String realm;

    @Column(nullable = false, length = 100)
    private String project;

    @Column(name = "base_url", nullable = false, length = 255)
    private String baseUrl;
}