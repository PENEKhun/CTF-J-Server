package com.penekhun.ctfjserver.User.Entity;

import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Table(name = "AuthLog", schema = "ctf")
public class AuthLog {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "idx", nullable = false)
    private Long idx;

    @ManyToOne @JoinColumn(name = "PROBLEM_IDX")
    //@Column(name = "problem_idx", nullable = false)
    private Problem problem;

    @ManyToOne @JoinColumn(name = "ACCOUNT_IDX")
    private Account solver;

    @Column(name = "account_idx", nullable = false)
    private Integer accountIdx;

    @Column(name = "auth_flag", nullable = false, length = 150)
    private String authFlag;

    @Column(name = "is_success", nullable = false)
    private Boolean isSuccess = false;

    public Boolean getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getAuthFlag() {
        return authFlag;
    }

    public void setAuthFlag(String authFlag) {
        this.authFlag = authFlag;
    }

    @Builder
    public AuthLog(Problem problem, Integer accountIdx, String authFlag, Boolean isSuccess) {
        this.problem = problem;
        this.accountIdx = accountIdx;
        this.authFlag = authFlag;
        this.isSuccess = isSuccess;
    }

}