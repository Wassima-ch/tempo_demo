package com.example.demo.inventoryservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@RestController
public class InventoryApp {
	public static void main(String[] args) { SpringApplication.run(InventoryApp.class, args); }

	@GetMapping("/inventory/{id}")
	public String check(@PathVariable String id) {
		return "IN_STOCK";
	}
}