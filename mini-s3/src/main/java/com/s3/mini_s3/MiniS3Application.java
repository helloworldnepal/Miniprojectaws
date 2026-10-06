package com.s3.mini_s3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MiniS3Application {

	public static void main(String[] args) {
		SpringApplication.run(MiniS3Application.class, args);
	}

}
