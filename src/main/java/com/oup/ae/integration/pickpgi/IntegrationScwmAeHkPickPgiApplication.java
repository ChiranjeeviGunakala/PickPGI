package com.oup.ae.integration.pickpgi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource("classpath:META-INF/spring/camel-route-config.xml")
public class IntegrationScwmAeHkPickPgiApplication {

	public static void main(String[] args) {
		SpringApplication.run(IntegrationScwmAeHkPickPgiApplication.class, args);
	}
}
