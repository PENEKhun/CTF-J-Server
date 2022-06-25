package com.penekhun.ctfjserver.Admin.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.validation.Valid;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class NoticeDto {

    @Data
    @Valid
    public static class Req{
        @Schema(description = "제목", required = true)
        private String title;
        @Schema(description = "내용", required = true)
        private String content;
    }

    @Getter
    public static class Res {
        @Schema(description = "로그리스트")
        @JsonProperty(value="")
        private final List<Item> noticeList;

        public void addNotice(Item log) {
            this.noticeList.add(log);
        }

        public Res() {
            this.noticeList = new ArrayList<>();
        }
    }


    @Data
    @Builder
    public static class Item {
        private Integer id;

        private String title;

        private String content;

        @CreatedDate
        @Column(name = "create_time", nullable = false)
        private Instant createTime;

        @LastModifiedDate
        @Column(name = "update_time")
        private Instant updateTime;
    }

}
