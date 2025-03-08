package com.reporthub;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@EnableEncryptableProperties
public class ReportHubApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReportHubApplication.class, args);
	}
}
