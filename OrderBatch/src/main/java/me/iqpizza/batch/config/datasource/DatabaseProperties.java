package me.iqpizza.batch.config.datasource;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "spring.datasource.batch")
public class DatabaseProperties {
    private String url;
    private String username;
    private String password;
    private String driverClassName;
}
