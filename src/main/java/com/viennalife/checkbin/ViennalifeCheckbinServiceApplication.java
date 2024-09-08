package com.viennalife.checkbin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ViennalifeCheckbinServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ViennalifeCheckbinServiceApplication.class, args);
	}

}
