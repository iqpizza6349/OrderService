package me.iqpizza.batch.config.datasource;

import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class DatabaseConfiguration {

    private final ModuleProperties moduleProperties;
    private final DatabaseProperties databaseProperties;

    @Bean
    @Qualifier("orderDataSource")
    public DataSource orderDataSource() {
        return DataSourceBuilder.create()
                .url(moduleProperties.getUrl())
                .username(moduleProperties.getUsername())
                .password(moduleProperties.getPassword())
                .driverClassName(moduleProperties.getDriverClassName())
                .type(HikariDataSource.class)
                .build();
    }

    @Bean
    @Primary
    @Qualifier("batchDataSource")
    public DataSource batchDataSource() {
        return DataSourceBuilder.create()
                .url(databaseProperties.getUrl())
                .username(databaseProperties.getUsername())
                .password(databaseProperties.getPassword())
                .driverClassName(databaseProperties.getDriverClassName())
                .type(HikariDataSource.class)
                .build();
    }
}
