package com.penekhun.ctfjserver;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CtfJServerApplication {

    private final ModelMapper modelMapper = new ModelMapper();

    public static void main(String[] args) {
        SpringApplication.run(CtfJServerApplication.class, args);
    }

}
