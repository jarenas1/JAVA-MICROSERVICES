package com.microservices.products.msvc_products;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
//entidades que se traen de dependencia
@EntityScan(basePackages = {"com.juan.libs.msvc.commons.libs_msvc_commons.entities"})
public class MsvcProductsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsvcProductsApplication.class, args);
	}

}
