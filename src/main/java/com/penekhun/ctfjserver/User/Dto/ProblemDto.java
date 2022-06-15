package com.penekhun.ctfjserver.User.Dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;

public class ProblemDto {

    @Data
    public static class Default{
//        @NotEmpty
        @Size(max=45)
        @NotBlank
        @ApiModelProperty(value = "문제 제목(max=45자)", required = true)
        private String title;

//        @NotEmpty
        @NotBlank
        @ApiModelProperty(value = "문제 설명", required = true)
        private String description;

//        @NotEmpty
        @Size(max=100)
        @NotBlank
        @ApiModelProperty(value = "플래그값", required = true, example = "FLAG{blaaa}")
        private String flag;

//        @NotEmpty
        @NotBlank
        @ApiModelProperty(value = "문제 타입(enum)", required = true, example = "Pwnable, Web, Reversing, Forensic, Crypto, Misc")
        private String type;

        @ApiModelProperty(value = "문제 공개 여부(Boolean)", required = true, example = "0, 1 or False, True")
        private Boolean isPublic;

        @NotNull
        @ApiModelProperty(value = "문제 기본 점수", required = true)
        private Integer defaultScore;

        private Instant modifyTime;
    }

    public static class Req{

        @Data
        @Valid
        public static class Auth{
            @NotBlank
            String flag;
        }

    }


    public static class Res{

        @Setter
        @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
        public static class correctProblemList {
            private Long problemIdx;
            private String title;
            private String description;
            private String type;
        }






    }
}
