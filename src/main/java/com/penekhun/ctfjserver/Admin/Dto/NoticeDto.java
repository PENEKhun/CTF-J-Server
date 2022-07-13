package com.penekhun.ctfjserver.Admin.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.penekhun.ctfjserver.User.Entity.Notice;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import javax.persistence.Column;
import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NoticeDto {

    @Data
    @Valid
    public static class Req{
        @Schema(description = "제목", required = true)
        private String title;
        @Schema(description = "내용", required = true)
        private String content;

        @Schema(description = "회원들에게 공지 생성 알림을 추가할지 여부", required = false, defaultValue = "false")
        private Boolean enableNotification = false;

        @Schema(description = "알림 제목", required = false)
        private String notificationTitle;

        @Schema(description = "알림 내용", required = false)
        private String notificationContent;

        public Notice toEntity(){
            return new Notice(title, content);
        }


        public Boolean isNotificationMode() {
            return Objects.requireNonNullElse(enableNotification, false);
        }
    }


    @Getter
    public static class Res {
        @Schema(description = "공지 목록")
        @JsonProperty(value="")
        private final List<Item> noticeList;

        public void addNotice(Item notice) {
            this.noticeList.add(notice);
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

        @Column(name = "create_time", nullable = false)
        private Timestamp createTime;
        @Column(name = "update_time")
        private Timestamp updateTime;
    }

}
