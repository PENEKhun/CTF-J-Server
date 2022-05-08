package com.penekhun.ctfjserver.User.Dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.Email;

@RequiredArgsConstructor
public class AccountDto {

    public static class Req{

        @Data
        @Valid
        public static class Signup{
            @Length(min = 3, max = 30)
            private String username;

            @Length(min = 8, max = 20)
            private String password;

            @Length(min = 3, max = 20)
            private String nickname;

            @Email
            private String email;

            @Length(min = 2, max = 10)
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

    }

    public class Res {

    }

}
