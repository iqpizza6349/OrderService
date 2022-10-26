package me.iqpizza;

import me.iqpizza.config.jwt.JwtProperties;
import me.iqpizza.config.jwt.JwtProvider;
import me.iqpizza.config.security.SecurityConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ConfigurationPropertiesScan(basePackageClasses = JwtProperties.class)
@ComponentScan(basePackageClasses = {SecurityConfig.class, JwtProvider.class})
public class OrderAPIApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderAPIApplication.class, args);
    }
}
