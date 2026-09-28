package com.qtm.ticket.client;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "qtmdb", url = "${qtmdb.feign.url:http://localhost:8080}", configuration = QtmdbFeignConfiguration.class)
public interface QtmdbClient {

    @GetMapping("/api/nurses")
    List<Map<String, Object>> getNurses(
            @RequestHeader("X-QTM-Realm") String realm,
            @RequestHeader("X-QTM-Project") String project);

    @GetMapping("/api/nurses/{id}")
    Map<String, Object> getNurse(
            @PathVariable("id") String id,
            @RequestHeader("X-QTM-Realm") String realm,
            @RequestHeader("X-QTM-Project") String project);

    @GetMapping("/api/doctors")
    List<Map<String, Object>> getDoctors(
            @RequestHeader("X-QTM-Realm") String realm,
            @RequestHeader("X-QTM-Project") String project);

    @GetMapping("/api/therapeutic-plans")
    List<Map<String, Object>> getTherapeuticPlans(
            @RequestHeader("X-QTM-Realm") String realm,
            @RequestHeader("X-QTM-Project") String project);

    @GetMapping("/api/therapeutic-plans/{id}")
    Map<String, Object> getTherapeuticPlan(
            @PathVariable("id") String id,
            @RequestHeader("X-QTM-Realm") String realm,
            @RequestHeader("X-QTM-Project") String project);
}