package web;

import filesearch.engine.file.FileSearchEngine;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.sql.DataSource;

@SpringBootApplication
@EnableScheduling
public class Application {

    private Config config;

    public Application(Config config) {
        this.config = config;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    FileSearchEngine fileSearchEngine() {
        return new FileSearchEngine(config.getSearchPath());
    }

    @Bean
    DataSource searchDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(config.getDbDriverClassName());
        dataSource.setUrl(config.getDbUrl());
        dataSource.setUsername(config.getDbUserName());
        dataSource.setPassword(config.getDbUserPassword());
        return dataSource;
    }
}