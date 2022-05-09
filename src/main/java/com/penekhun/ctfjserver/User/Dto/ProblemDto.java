package com.penekhun.ctfjserver.User.Dto;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;

public class ProblemDto {

    public static class Default{
        @NotEmpty
        @Size(max=45)
        private String title;

        @NotEmpty
        private String description;

        @NotEmpty
        @Size(max=100)
        private String flag;

        @NotEmpty
        private String type;

        private Boolean isPublic;

        @NotNull
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

    }
}
