package com.qtm.ticket.client;

import com.qtm.commonlib.api.GeographyApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "qtm-geography-service", url = "${services.qtm-geography-service.url}")
public interface QtmGeographyClient extends GeographyApi {
}