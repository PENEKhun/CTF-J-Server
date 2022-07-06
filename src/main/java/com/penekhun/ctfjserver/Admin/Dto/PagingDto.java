package com.penekhun.ctfjserver.Admin.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

public class PagingDto {

    @Data
    public static class Log {
        @Schema(description = "페이지 넘버", required = false, defaultValue = "1")
        private int pageNum = 1;
        @Schema(description = "페이지 당 불러올 로그 개수", required = false, defaultValue = "10")
        private int amount = 10;

                @Schema(description = "불러올 로그의 타입", required = false, defaultValue = "log", example = "")
        private String logType = "log";
        @Schema(description = "검색시 타입", required = false)
        private String keyword;

        @Schema(description = "정렬 방식", required = false, defaultValue = "desc", example = "{desc, asc}")
        private String sortType = "desc";
    }

    @Data
    public static class Account {
        @Schema(description = "페이지 넘버", required = false, defaultValue = "1")
        private int pageNum = 1;
        @Schema(description = "페이지 당 불러올 로그 개수", required = false, defaultValue = "10")
        private int amount = 10;

        @Schema(description = "검색시 타입", required = false)
        private String keyword;

        @Schema(description = "정렬 방식", required = false, defaultValue = "desc", example = "{desc, asc}")
        private String sortType = "desc";
    }
}
