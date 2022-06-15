package com.penekhun.ctfjserver;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableCaching
@EnableScheduling
@EnableJpaAuditing
@SpringBootApplication
public class CtfJServerApplication {

    private final ModelMapper modelMapper = new ModelMapper();

    public static void main(String[] args) {
        SpringApplication.run(CtfJServerApplication.class, args);
    }


    @Bean //해당 메서드의 리턴되는 오브젝트를 IoC로 등록함
    public BCryptPasswordEncoder encodePwd(){
        return new BCryptPasswordEncoder();
    }


    @Bean
    public ModelMapper modelMapper() {
        modelMapper.getConfiguration()
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE)
                .setFieldMatchingEnabled(true)
                //setter없이 modelMapper 사용

                .setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper;
    }

}
