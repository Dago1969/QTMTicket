package com.qtm.ticket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * QTM Ticket Service - Centralized ticket management application.
 */
@SpringBootApplication
@EntityScan(basePackages = {"com.qtm.ticket.entity", "com.qtm.commonlib.entity"})
@EnableJpaRepositories(basePackages = {"com.qtm.ticket.repository"})
@EnableFeignClients(basePackages = "com.qtm.ticket.client")
public class QtmTicketApplication {

    public static void main(String[] args) {
        SpringApplication.run(QtmTicketApplication.class, args);
    }
}
