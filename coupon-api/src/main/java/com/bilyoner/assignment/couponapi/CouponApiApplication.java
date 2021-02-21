package com.bilyoner.assignment.couponapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.time.Duration;

@EnableSwagger2
@SpringBootApplication
public class CouponApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(CouponApiApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {

        return builder
                .setConnectTimeout(Duration.ofMillis(1000))
                .setReadTimeout(Duration.ofMillis(1000))
                .build();
    }
}
