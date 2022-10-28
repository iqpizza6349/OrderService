package me.iqpizza.batch.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class OrderBatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderBatchApplication.class, args);
    }
}
