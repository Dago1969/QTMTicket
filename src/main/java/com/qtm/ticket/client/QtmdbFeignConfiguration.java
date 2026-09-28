package com.qtm.ticket.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.RequestInterceptor;

import com.qtm.ticket.service.RealmEndpointService;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class QtmdbFeignConfiguration {

    @Bean
    RequestInterceptor qtmdbEndpointInterceptor(RealmEndpointService endpointService) {
        return template -> {
            String realm = firstHeader(template, "X-QTM-Realm");
            String project = firstHeader(template, "X-QTM-Project");
            if (realm == null || project == null) {
                throw new IllegalArgumentException("Gli header X-QTM-Realm e X-QTM-Project sono obbligatori");
            }
            template.target(endpointService.resolveBaseUrl(realm, project));
        };
    }

    private static String firstHeader(feign.RequestTemplate template, String name) {
        return template.headers().getOrDefault(name, java.util.Collections.emptyList())
                .stream()
                .findFirst()
                .orElse(null);
    }
}