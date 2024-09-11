package com;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PujaCanadaApplication {
	private static final Logger logger = LogManager.getLogger(PujaCanadaApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(PujaCanadaApplication.class, args);
		System.out.println("Jai Sree Ram");
		logger.info("Application started successfully.");
		logger.debug("This is a debug message.");
		logger.error("This is an error message.");

	}
	
	

}
