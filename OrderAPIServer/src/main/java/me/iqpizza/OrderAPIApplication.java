package me.iqpizza;

import me.iqpizza.config.datasource.StaffDatabaseProperties;
import me.iqpizza.config.jwt.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@ConfigurationPropertiesScan(basePackageClasses = {JwtProperties.class, StaffDatabaseProperties.class})
public class OrderAPIApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderAPIApplication.class, args);
    }
}
