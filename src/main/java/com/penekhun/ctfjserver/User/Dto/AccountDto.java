package com.penekhun.ctfjserver.User.Dto;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.Email;

@RequiredArgsConstructor
public class AccountDto {

    public static class Req{

        @Data
        @Valid
        public static class Signup{
            @Schema(description = "3~30자사이 아이디")
            @ApiModelProperty(value = "3~30자사이 아이디", required = true)
            @Length(min = 3, max = 30)
            private String username;

            @Schema(description = "8~20사이 패스워드")
            @ApiModelProperty(value = "8~20사이 패스워드", required = true)
            @Length(min = 8, max = 20)
            private String password;

            @Schema(description = "3~20자 사이 닉네임")
            @ApiModelProperty(value = "3~20자 사이 닉네임", required = true)
            @Length(min = 3, max = 20)
            private String nickname;

            @Email
            @Schema(description = "이메일")
            @ApiModelProperty(value = "이메일", required = true)
            private String email;

            @Length(min = 2, max = 10)
            @ApiModelProperty(value = "2~10자 사이 실명", required = true)
            private String realName;

            @Builder
            public Signup(String username, String password, String nickname, String email, String realName) {
                this.username = username;
                this.password = password;
                this.nickname = nickname;
                this.email = email;
                this.realName = realName;
            }
        }

        @Data
        public static class Login {
            @ApiModelProperty(value = "아이디")
            private String username;
            @ApiModelProperty(value = "비밀번호")
            private String password;
        }

    }

    public static class Res {

        @Data
        public static class Signup{
            @ApiModelProperty(value = "아이디", required = true)
            private String username;
            @ApiModelProperty(value = "닉네임", required = true)
            private String nickname;
            @ApiModelProperty(value = "이메일", required = true)
            private String email;
            @ApiModelProperty(value = "실명", required = true)
            private String realName;

        }


    }

}
