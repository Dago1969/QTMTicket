package com.qtm.ticket.client;

import com.qtm.commonlib.api.HealthStructureApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
        name = "qtm-health-structure-service",
        url = "${QTM_HEALTH_STRUCTURE_SERVICE_URL:http://localhost:8089/api/health-structure}",
        configuration = HealthStructureFeignConfiguration.class
)
public interface HealthStructureClient extends HealthStructureApi {
}