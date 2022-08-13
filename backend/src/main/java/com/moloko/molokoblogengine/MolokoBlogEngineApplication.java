package com.moloko.molokoblogengine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@EnableReactiveMongoRepositories
@SpringBootApplication
public class MolokoBlogEngineApplication {

	public static void main(String[] args) {
		SpringApplication.run(MolokoBlogEngineApplication.class, args);
	}
}
