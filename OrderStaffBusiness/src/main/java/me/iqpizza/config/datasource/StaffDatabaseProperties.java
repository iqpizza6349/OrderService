package me.iqpizza.config.datasource;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "staff.datasource")
public class StaffDatabaseProperties {
    private String url;
    private String username;
    private String password;
    private String driverClassName;
}
