package com.example.shipbrowser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ShipBrowserApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShipBrowserApplication.class, args);
    }

}
