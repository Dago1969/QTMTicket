package com.qtm.ticket.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.qtm.ticket.entity.RealmEndpoint;

public interface RealmEndpointRepository extends JpaRepository<RealmEndpoint, Long> {

    Optional<RealmEndpoint> findByRealmAndProject(String realm, String project);
}