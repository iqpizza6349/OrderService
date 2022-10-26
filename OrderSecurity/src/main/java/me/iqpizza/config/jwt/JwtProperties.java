package me.iqpizza.config.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("order.jwt")
public class JwtProperties {
    private String secret;
    private String refresh;
    private long expirationSecond;
}
