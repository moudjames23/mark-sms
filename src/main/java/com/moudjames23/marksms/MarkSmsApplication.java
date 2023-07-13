package com.moudjames23.marksms;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MarkSmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(MarkSmsApplication.class, args);
    }

    @Bean
    ModelMapper modelMapper() {
        return  new ModelMapper();
    }
}
