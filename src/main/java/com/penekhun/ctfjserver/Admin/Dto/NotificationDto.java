package com.penekhun.ctfjserver.Admin.Dto;

import com.penekhun.ctfjserver.Config.Exception.CustomException;
import com.penekhun.ctfjserver.Config.Exception.ErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import org.springframework.core.convert.converter.Converter;

import javax.validation.Valid;

public class NotificationDto {

    @Data
    @Valid
    public static class Req{
        @Schema(description = "제목", required = true)
        private String title;
        @Schema(description = "내용", required = true)
        private String content;

        @Builder
        public Req(String title, String content) {
            this.title = title;
            this.content = content;
        }

        @Schema(description = "알림 모드", required = false, deprecated = true)
        private String mode;

        public NotificationMode getNotificationMode() {
            return NotificationMode.of(mode);
        }
    }

    public enum NotificationMode{
        ALL("WITHOUT_ADMIN", "관리자 빼고 전부"), SPECIFIC("SPECIFIC_TARGET", "특정 대상");

        private final String modeName;

        NotificationMode(String modeName, final String description) {
            this.modeName = modeName;
//            description = 해당 모드에 대한 설명
        }


        public static NotificationMode of(String modeName){
            if (modeName == null)
                throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);

                for(NotificationMode nm : NotificationMode.values()){
                    if (nm.modeName.equalsIgnoreCase(modeName))
                        return nm;
                }
            // 없을시
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
        }
    }

    public class StringToNotificationModeConverter implements Converter<String, NotificationMode> {
        @Override
        public NotificationMode convert(String modeName) {
            return NotificationMode.of(modeName);
        }
    }

}
