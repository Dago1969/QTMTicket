package com.qtm.ticket.client;

import com.qtm.commonlib.api.HealthStructureApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
        name = "qtm-health-structure-service",
        url = "${services.qtm-health-structure-service.url}",
        configuration = HealthStructureFeignConfiguration.class
)
public interface HealthStructureClient extends HealthStructureApi {
}