package com.penekhun.ctfjserver.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); //서버가 응답할때 json을 자바 스크립트에서 처리 할 수 있게 할지 설정
        config.addAllowedOrigin("*"); //모든 ip에 응답을 허용
        config.addAllowedHeader("*"); //모든 header에 응답을 허용
        config.addAllowedMethod("*"); //모든 method 허용
        source.registerCorsConfiguration("/api/**", config);
        //api로 들어오는 요청은 위 정책들을 따름
        return new CorsFilter(source);
    }
}
