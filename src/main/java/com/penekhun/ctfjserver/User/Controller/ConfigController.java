package com.penekhun.ctfjserver.User.Controller;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/config")
@Slf4j
@RequiredArgsConstructor
public class ConfigController {

    @Value("${server.time-zone}") String timeZone;
    @Value("${server.enable-open-timer}") Boolean enableOpenTimer;
    @Value("${server.open-time-format}") String openTimeFormat;
    @Value("${server.open-time}") String openTime;
    @Value("${server.end-time-format}") String endTimeFormat;
    @Value("${server.end-time}") String endTime;

    @Value("${jwt.token-validity-in-seconds}") int tokenValidityInSeconds;
    @Value("${jwt.refresh-token-validity-in-seconds}") int refreshTokenValidityInSeconds;

    @Value("${spring.servlet.multipart.max-file-size}") String maxFileSize;
    @Value("${spring.servlet.multipart.max-request-size}") String maxRequestSize;


    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public ConfigResponse configMapping(){
        return ConfigResponse.builder()
                .timeZone(timeZone)
                .enableOpenTimer(enableOpenTimer)
                .openTimeFormat(openTimeFormat)
                .openTime(openTime)
                .endTimeFormat(endTimeFormat)
                .endTime(endTime)
                .tokenValidityInSeconds(tokenValidityInSeconds)
                .refreshTokenValidityInSeconds(refreshTokenValidityInSeconds)
                .maxFileSize(maxFileSize)
                .maxRequestSize(maxRequestSize)
                .build();
    }


    @Data
    @Builder
    public static class ConfigResponse {
        String timeZone;
        Boolean enableOpenTimer;
        String openTimeFormat;
        String openTime;
        String endTimeFormat;
        String endTime;

        int tokenValidityInSeconds;
        int refreshTokenValidityInSeconds;

        String maxFileSize;
        String maxRequestSize;
    }

}
