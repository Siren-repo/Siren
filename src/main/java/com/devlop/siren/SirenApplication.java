package com.devlop.siren;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class SirenApplication {

	public static void main(String[] args) {
		SpringApplication.run(SirenApplication.class, args);
	}

}
