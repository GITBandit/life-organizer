package pl.java.lifeorganizer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan("pl.java.lifeorganizer")
@SpringBootApplication
public class LifeOrganizerApplication {

	public static void main(String[] args) {
		SpringApplication.run(LifeOrganizerApplication.class, args);
	}
}
