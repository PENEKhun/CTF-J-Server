package com.penekhun.ctfjserver.User.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.penekhun.ctfjserver.Config.SecurityRole;
import com.penekhun.ctfjserver.User.Entity.Account;
import com.penekhun.ctfjserver.Util.RankSchedule;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
public class AccountDto {

    public static class Req{

        public static class ValidationGroups{
            public interface noValid {};
            public interface checkFullValid {};
            public interface checkOnlyPassword {};
        }

        @Data
        @Valid
        public static class Signup{
            @Schema(description = "3~30자사이 아이디", required = true)
            @Length(min = 3, max = 30, groups = ValidationGroups.checkFullValid.class)
            private String username;

            @Schema(description = "8~20사이 패스워드", required = true)
            @Length(min = 8, max = 20, groups = {ValidationGroups.checkFullValid.class, ValidationGroups.checkOnlyPassword.class})
            private String password;

            @Schema(description = "3~20자 사이 닉네임", required = true)
            @Length(min = 3, max = 20, groups = ValidationGroups.checkFullValid.class)
            private String nickname;

            @Email(groups = ValidationGroups.checkFullValid.class)
            @Schema(description = "이메일", required = true)
            private String email;

            @Length(min = 2, max = 10, groups = ValidationGroups.checkFullValid.class)
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
                this.userRole = Objects.requireNonNullElse(role, SecurityRole.USER);
            }

            public Account toEntity(){
                BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
                return Account.builder()
                        .username(this.getUsername())
                        .password(bCryptPasswordEncoder.encode(this.getPassword()))
                        .email(this.getEmail())
                        .nickname(this.getNickname())
                        .role(this.userRole)
                        .realName(this.getRealName()).build();
            }
        }

        @Validated(ValidationGroups.noValid.class) //그룹 지정안함 = WithoutValid
        public static class SignupWithoutValid extends Signup {

            public SignupWithoutValid(String username, String password, String nickname, String email, String realName, SecurityRole role) {
                super(username, password, nickname, email, realName, role);
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
        public static class AccountList{
            private List<MyPage> accounts;

            @JsonProperty(value="totalPage")
            private final int totalPage;
            @JsonProperty(value="totalElements")
            private final long totalElements;

            public AccountList(int totalPage, long totalElements) {
                this.accounts = new ArrayList<>();
                this.totalPage = totalPage;
                this.totalElements = totalElements;
            }

            public void addAccount(MyPage account){
                this.accounts.add(account);
            }

        }

        @Data
        @Builder
        @AllArgsConstructor
        public static class MyPage{
            @Schema(description = "index")
            private Long id;
            @Schema(description = "아이디")
            private String username;
            @Schema(description = "닉네임")
            private String nickname;
            @Schema(description = "실명")
            private String realName;
            @Schema(description = "이메일")
            private String email;
            @Schema(description = "점수")
            private Integer score;
            @Schema(description = "사용자 권한")
            private SecurityRole role;
            @Schema(description = "푼 문제 리스트")
            private List<ProblemDto.Res.CorrectProblem> solved;


            public static MyPage valueOf(Account account){
                Optional<RankDto.AccountSolveProbList> accSolveProb =
                        RankSchedule.accSolveList.stream()
                                .filter(acc -> acc.getAccountId().equals(account.getId()))
                                .findAny();

                MyPage myPage = new MyPageBuilder().email(account.getEmail())
                        .nickname(account.getNickname())
                        .role(account.getRole())
                        .id(account.getId())
                        .username(account.getUsername())
                        .realName(account.getRealName())
                        .build();

                if (accSolveProb.isEmpty()){
                    myPage.setScore(0);
                } else {
                    myPage.setScore(accSolveProb.get().getScore());
                }

                return myPage;
            }


        }


    }

}
