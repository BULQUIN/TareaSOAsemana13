package pe.edu.utp.articulosapi3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class Articulosapi3Application {

	public static void main(String[] args) {
		SpringApplication.run(Articulosapi3Application.class, args);
	}

}
