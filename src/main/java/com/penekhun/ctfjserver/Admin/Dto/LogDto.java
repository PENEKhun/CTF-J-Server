package com.penekhun.ctfjserver.Admin.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class LogDto {

    @Data
    @Valid
    public static class Req{
        @Schema(description = "페이지 넘버", required = false, defaultValue = "1")
        private int pageNum = 1;
        @Schema(description = "페이지 당 불러올 로그 개수", required = false, defaultValue = "10")
        private int amount = 10;

        @Schema(description = "불러올 로그의 타입", required = false, defaultValue = "log", example = "{flag, log}")
        private String logType = "log";
        @Schema(description = "검색시 타입", required = false)
        private String keyword;
    }

    @Getter
    public static class Res {
        @Schema(description = "로그리스트")
        @JsonProperty(value="log")
        private final List<Item> logList;

        public void addLog(Item log) {
            this.logList.add(log);
        }

        public Res() {
            this.logList = new ArrayList<>();
        }
    }


    @Data
    @Builder
    public static class Item {
        @Schema(description = "로그 index", required = false)
        private Long id;
        @Schema(description = "로그의 행위자의 index (서버측 오류는 0)", required = false)
        private Long accountIdx;
        @Schema(description = "짧은 로그", required = false)
        private String action;
        @Schema(description = "구체적인 로그", required = false)
        private String detail;
        @Schema(description = "로그 발생 시각", required = false)
        private Timestamp time;
    }

}
