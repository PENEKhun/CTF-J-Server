package com.penekhun.ctfjserver.User.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;

public class ProblemDto {

    public static class ValidationGroups{
        public interface noValid {};
        public interface checkFullValid {};
    }

    @Data
    @Builder
    public static class Default{
//        @NotEmpty
        @Size(max=45)
        @NotBlank(groups = ValidationGroups.checkFullValid.class)
        @Schema(description = "문제 제목(max=45자)", required = true)
        private String title;

//        @NotEmpty
        @NotBlank(groups = ValidationGroups.checkFullValid.class)
        @Schema(description = "문제 설명", required = true)
        private String description;

//        @NotEmpty
        @Size(max=100)
        @NotBlank(groups = ValidationGroups.checkFullValid.class)
        @Schema(description = "플래그값", required = true, example = "FLAG{blaaa}")
        private String flag;

//        @NotEmpty
        @NotBlank(groups = ValidationGroups.checkFullValid.class)
        @Schema(description = "문제 타입(enum)", required = true, example = "Pwnable, Web, Reversing, Forensic, Crypto, Misc")
        private String type;

        @JsonProperty(value = "isPublic")
        @Schema(description = "문제 공개 여부(Boolean)", required = false, example = "0, 1 or False, True")
        private Boolean isPublic;

        @NotNull(groups = ValidationGroups.checkFullValid.class)
        @Schema(description = "문제 최고 점수", required = true)
        private Integer maxScore;

        @NotNull(groups = ValidationGroups.checkFullValid.class)
        @Schema(description = "문제 최저 점수", required = true)
        private Integer minScore;

        @NotNull(groups = ValidationGroups.checkFullValid.class)
        @Schema(description = "solve 한계치", required = true)
        private Integer solveThreshold;

        @Schema(accessMode = Schema.AccessMode.READ_ONLY)
        private Timestamp modifyTime;

        private Integer fileIdx;

        public boolean isValidScore(){
            return this.minScore >= 0 && this.maxScore >= 0 && this.solveThreshold >= 0 && this.minScore <= this.maxScore;
        }
    }

    @Validated(ValidationGroups.noValid.class)
    public static class DefaultNoValid extends Default{
        DefaultNoValid(@Size(max = 45) @NotBlank(groups = ValidationGroups.checkFullValid.class) String title, @NotBlank(groups = ValidationGroups.checkFullValid.class) String description, @Size(max = 100) @NotBlank(groups = ValidationGroups.checkFullValid.class) String flag, @NotBlank(groups = ValidationGroups.checkFullValid.class) String type, Boolean isPublic, @NotNull(groups = ValidationGroups.checkFullValid.class) Integer maxScore, @NotNull(groups = ValidationGroups.checkFullValid.class) Integer minScore, @NotNull(groups = ValidationGroups.checkFullValid.class) Integer solveThreshold, Timestamp modifyTime, Integer fileIdx) {
            super(title, description, flag, type, isPublic, maxScore, minScore, solveThreshold, modifyTime, fileIdx);
        }
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
            private Long id;
            private String title;
            private String type;
        }

        @Data
        public static class File{
            private String url;
        }






    }
}
