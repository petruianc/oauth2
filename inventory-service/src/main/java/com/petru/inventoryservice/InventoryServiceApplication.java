package com.petru.inventoryservice;

import com.petru.inventoryservice.model.Inventory;
import com.petru.inventoryservice.repository.InventoryRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}


	@Bean
	public CommandLineRunner loadData(InventoryRepo inventoryRepo){
		return args -> {
			Inventory inventory = new Inventory();
			inventory.setSkuCode("smasung S23");
			inventory.setQuantity(100);

			Inventory inventory1 = new Inventory();
			inventory1.setSkuCode("smasung S23 red");
			inventory1.setQuantity(0);

			inventoryRepo.save(inventory);
			inventoryRepo.save(inventory1);
		};
	}
}
