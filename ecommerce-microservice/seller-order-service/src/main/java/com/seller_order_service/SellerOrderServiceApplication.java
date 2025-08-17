package com.seller_order_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class SellerOrderServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SellerOrderServiceApplication.class, args);
	}

}
