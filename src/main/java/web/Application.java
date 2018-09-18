package web;

import filesearch.engine.file.FileSearchEngine;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

    private Config config;

    public Application(Config config) {
        this.config = config;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    String searchPath() {
        return config.getSearchPath();
    }

    @Bean
    FileSearchEngine fileSearchEngine(String searchPath) {
        return new FileSearchEngine(searchPath);
    }
}