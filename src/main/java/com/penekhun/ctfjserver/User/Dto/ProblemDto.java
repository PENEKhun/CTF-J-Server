package com.penekhun.ctfjserver.User.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;

public class ProblemDto {

    @Data
    @Builder
    public static class Default{
//        @NotEmpty
        @Size(max=45)
        @NotBlank
        @Schema(description = "문제 제목(max=45자)", required = true)
        private String title;

//        @NotEmpty
        @NotBlank
        @Schema(description = "문제 설명", required = true)
        private String description;

//        @NotEmpty
        @Size(max=100)
        @NotBlank
        @Schema(description = "플래그값", required = true, example = "FLAG{blaaa}")
        private String flag;

//        @NotEmpty
        @NotBlank
        @Schema(description = "문제 타입(enum)", required = true, example = "Pwnable, Web, Reversing, Forensic, Crypto, Misc")
        private String type;

        @Schema(description = "문제 공개 여부(Boolean)", required = true, example = "0, 1 or False, True")
        private Boolean isPublic;

        @NotNull
        @Schema(description = "문제 최고 점수", required = true)
        private Integer maxScore;

        @NotNull
        @Schema(description = "문제 최저 점수", required = true)
        private Integer minScore;

        @NotNull
        @Schema(description = "solve 한계치", required = true)
        private Integer solveThreshold;

        @Schema(accessMode = Schema.AccessMode.READ_ONLY)
        private Timestamp modifyTime;
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

        @Data
        @Builder
        public static class CorrectProblem {
            private Integer id;
            private String title;
            private String type;
        }

        @Data
        public static class File{
            private String url;
        }






    }
}
