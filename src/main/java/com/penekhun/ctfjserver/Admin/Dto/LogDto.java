package com.penekhun.ctfjserver.Admin.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class LogDto {

    @Getter
    public static class Res {
        @Schema(description = "로그리스트")
        @JsonProperty(value="log")
        private final List<Item> logList;

        @JsonProperty(value="totalPage")
        private final int totalPage;
        @JsonProperty(value="totalElements")
        private final long totalElements;

        public void addLog(Item log) {
            this.logList.add(log);
        }

        public Res(int totalPage, long totalElements) {
            this.logList = new ArrayList<>();
            this.totalPage = totalPage;
            this.totalElements = totalElements;

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
