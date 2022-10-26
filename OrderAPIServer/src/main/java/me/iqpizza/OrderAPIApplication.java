package me.iqpizza;

import me.iqpizza.config.jwt.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan(basePackageClasses = JwtProperties.class)
public class OrderAPIApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderAPIApplication.class, args);
    }
}
