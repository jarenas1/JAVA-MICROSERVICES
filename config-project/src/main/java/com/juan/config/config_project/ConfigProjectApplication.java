package com.juan.config.config_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer
@SpringBootApplication
public class ConfigProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConfigProjectApplication.class, args);
	}

}
