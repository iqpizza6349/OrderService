package me.iqpizza.global.config.datasource;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "spring.datasource")
public class OrderDatabaseProperties {
    private String url;
    private String username;
    private String password;
    private String driverClassName;
}