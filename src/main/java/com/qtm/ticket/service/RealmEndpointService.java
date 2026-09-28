package com.qtm.ticket.service;

import org.springframework.stereotype.Service;

import com.qtm.ticket.entity.RealmEndpoint;
import com.qtm.ticket.repository.RealmEndpointRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RealmEndpointService {

    private final RealmEndpointRepository repository;

    public String resolveBaseUrl(String realm, String project) {
        return repository.findByRealmAndProject(realm, project)
                .map(RealmEndpoint::getBaseUrl)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Nessun endpoint configurato per realm=" + realm + ", project=" + project));
    }
}