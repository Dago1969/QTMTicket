package com.qtm.ticket.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "departments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "area_funzionale", length = 200, nullable = false)
    private String areaFunzionale;

    @Column(name = "reparto", length = 200, nullable = false)
    private String reparto;

    @Column(name = "main_responsibilities", columnDefinition = "TEXT")
    private String mainResponsibilities;

    @Column(name = "example_symptoms", columnDefinition = "TEXT")
    private String exampleSymptoms;

    @Column(name = "emergenza_urgenza")
    private Boolean emergenzaUrgenza;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
