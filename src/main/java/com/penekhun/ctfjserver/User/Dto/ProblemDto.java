package com.penekhun.ctfjserver.User.Dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
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
        private String title;

//        @NotEmpty
        @NotBlank
        private String description;

//        @NotEmpty
        @Size(max=100)
        @NotBlank
        private String flag;

//        @NotEmpty
        @NotBlank
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

  /*      @Data
        @Valid
        public static class MakeProblem extends DefaultType{

        }*/


    }


    public static class Res{

        @Setter
        @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
        public static class problemWithoutFlag {
            private String title;
            private String description;
            private String type;
            private Boolean isPublic;
            private Integer defaultScore;
            private Instant modifyTime;
        }






    }
}
