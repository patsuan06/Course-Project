package com.trinity.crourierapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;


@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})public class CrourierAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrourierAppApplication.class, args);
    }

}
