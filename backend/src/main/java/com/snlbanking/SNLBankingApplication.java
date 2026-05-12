package com.snlbanking;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Main Spring Boot Application for SNL Banking System
 * 
 * @author SNL Banking Team
 * @version 1.0.0
 */
@SpringBootApplication
@EnableJpaAuditing
public class SNLBankingApplication {

    public static void main(String[] args) {
        SpringApplication.run(SNLBankingApplication.class, args);
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("SNL Banking API")
                        .version("1.0.0")
                        .description("Comprehensive Banking Application API - HDFC Reference Implementation"));
    }
}
