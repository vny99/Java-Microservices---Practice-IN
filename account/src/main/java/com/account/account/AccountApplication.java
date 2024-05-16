package com.account.account;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.client.WebClient;

import io.r2dbc.spi.ConnectionFactory;

@SpringBootApplication
@EnableR2dbcRepositories
@EnableWebFlux
@EnableDiscoveryClient
public class AccountApplication {

	@Bean
	WebClient.Builder getWebClientBuilder() {
		return WebClient.builder().baseUrl("http://localhost:8082/alianz/purchase");
	}

	@Bean
	public ConnectionFactoryInitializer initializer(
			@Qualifier("connectionFactory") ConnectionFactory connectionFactory) {
		ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
		initializer.setConnectionFactory(connectionFactory);
		ResourceDatabasePopulator resource = new ResourceDatabasePopulator(new ClassPathResource("schema.sql"));
		initializer.setDatabasePopulator(resource);
		return initializer;
	}

	public static void main(String[] args) {
		SpringApplication.run(AccountApplication.class, args);
	}

}
