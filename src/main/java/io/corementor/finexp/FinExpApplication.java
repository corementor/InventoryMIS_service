package io.corementor.finexp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"io.corementor.finexp","psychemesh.framework.*"})
public class FinExpApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinExpApplication.class, args);
	}

}
