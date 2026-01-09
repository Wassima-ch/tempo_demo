package com.example.demo.orderservice;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class OrderApp {
    public static void main(String[] args) { SpringApplication.run(OrderApp.class, args); }

    @Bean // Crucial for tracing to pass through RestTemplate
    public RestTemplate restTemplate(RestTemplateBuilder builder) { return builder.build(); }
}

@RestController
class OrderController {
    // Add the logger factory
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(OrderController.class);

    private final RestTemplate rest;
    public OrderController(RestTemplate rest) { this.rest = rest; }

    @GetMapping("/checkout/{id}")
    public String checkout(@PathVariable String id) {
        // Add this line:
        log.info("Processing checkout for ID: {}", id);

        String inv = rest.getForObject("http://inventory-service:8082/inventory/"+id, String.class);
        String pay = rest.getForObject("http://payment-service:8083/payment/"+id, String.class);
        return "Order Status: " + inv + " & " + pay;
    }
}