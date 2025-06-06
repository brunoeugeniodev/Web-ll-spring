package com.example.PWIIJava.config;

import com.example.PWIIJava.service.FilmeService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {

    @Bean
    public FilmeService filmeService() {
        return Mockito.mock(FilmeService.class);
    }

}