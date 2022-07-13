package com.penekhun.ctfjserver;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.ZoneId;
import java.util.TimeZone;

import static com.penekhun.ctfjserver.Util.RankSchedule.RANK_HISTORY_FILENAME;
import static com.penekhun.ctfjserver.Util.RankSchedule.everyHourScoreRank;

@EnableCaching
@EnableScheduling
@EnableJpaAuditing
@SpringBootApplication
@Slf4j
public class CtfJServerApplication {

    private final ModelMapper modelMapper = new ModelMapper();
    @Value("${server.time-zone}") String timeZone;

    public static void main(String[] args) {
        SpringApplication.run(CtfJServerApplication.class, args);
    }


    @Bean //해당 메서드의 리턴되는 오브젝트를 IoC로 등록함
    public BCryptPasswordEncoder encodePwd(){
        return new BCryptPasswordEncoder();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void init() {


        TimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of(timeZone)));
        log.info("Set Default Timezone as {}", ZoneId.of(timeZone));

        log.info("loading rank history.... in local file");
        Gson gson = new Gson();
        try {
            if (Files.exists(Path.of(RANK_HISTORY_FILENAME))) {
                JsonReader reader = new JsonReader(new FileReader(RANK_HISTORY_FILENAME));
                everyHourScoreRank = gson.fromJson(reader, everyHourScoreRank.getClass());
                log.info("success loaded Rank history....");
            }
        } catch (FileNotFoundException e) {
            log.error("Rank History File load Error!!!!");
        }
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
