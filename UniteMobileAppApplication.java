package com.Unite.UniteMobileApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// ✅ Add these two imports for logging
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class UniteMobileAppApplication {

	// ✅ Add this logger declaration
	private static final Logger logger = LoggerFactory.getLogger(UniteMobileAppApplication.class);

	public static void main(String[] args) {
		logger.info(">>> Starting UniteMobileAppApplication...");  // ✅ Now IntelliJ can resolve this
		SpringApplication.run(UniteMobileAppApplication.class, args);
	}
}
