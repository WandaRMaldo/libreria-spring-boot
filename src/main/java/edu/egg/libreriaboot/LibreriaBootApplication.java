package edu.egg.libreriaboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class LibreriaBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibreriaBootApplication.class, args);
	}

}
