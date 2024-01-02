package com.komponente.servis2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ScheduleService {

        public static void main(String[] args) {
            SpringApplication.run(ScheduleService.class, args);
        }
}
