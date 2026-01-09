package com.example.demo.paymentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@RestController
public class PaymentApp {
    public static void main(String[] args) { SpringApplication.run(PaymentApp.class, args); }

    @GetMapping("/payment/{id}"

    public String pay(@PathVariable String id) throws InterruptedException {
        if (id.equals("0")) throw new RuntimeException("Bank API Connection Refused!");
        if (id.equals("99")) Thread.sleep(5000);
        return "PAYMENT_SUCCESSFUL";
    }
}