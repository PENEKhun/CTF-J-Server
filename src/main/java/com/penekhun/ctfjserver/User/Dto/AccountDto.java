package com.penekhun.ctfjserver.User.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import java.util.List;

@RequiredArgsConstructor
public class AccountDto {

    public static class Req{

        @Data
        @Valid
        public static class Signup{
            @Schema(description = "3~30자사이 아이디", required = true)
            @Length(min = 3, max = 30)
            private String username;

            @Schema(description = "8~20사이 패스워드", required = true)
            @Length(min = 8, max = 20)
            private String password;

            @Schema(description = "3~20자 사이 닉네임", required = true)
            @Length(min = 3, max = 20)
            private String nickname;

            @Email
            @Schema(description = "이메일", required = true)
            private String email;

            @Length(min = 2, max = 10)
            @Schema(description = "2~10자 사이 실명", required = true)
            private String realName;

            @Schema(description = "유저 권한", required = false, defaultValue = "USER")
            private SecurityRole userRole;

            @Builder
            public Signup(String username, String password, String nickname, String email, String realName, SecurityRole role) {
                this.username = username;
                this.password = password;
                this.nickname = nickname;
                this.email = email;
                this.realName = realName;
                this.userRole = role;
            }
        }

        @Data
        public static class Login {
            @Schema(description = "아이디", required = true)
            private String username;
            @Schema(description = "비밀번호", required = true)
            private String password;
        }

    }

    public static class Res {

        @Data
        public static class Signup{
            @Schema(description = "아이디", required = true)
            private String username;
            @Schema(description = "닉네임", required = true)
            private String nickname;
            @Schema(description = "이메일", required = true)
            private String email;
            @Schema(description = "실명", required = true)
            private String realName;

        }

        @Data
        public static class MyPage{
            @Schema(description = "index")
            private Integer id;
            @Schema(description = "아이디")
            private String username;
            @Schema(description = "닉네임")
            private String nickname;
            @Schema(description = "이메일")
            private String email;
            @Schema(description = "점수")
            private Integer score;
            @Schema(description = "푼 문제 리스트")
            private List<ProblemDto.Res.CorrectProblem> solved;


        }


    }

}
