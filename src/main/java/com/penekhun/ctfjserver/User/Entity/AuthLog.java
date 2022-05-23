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

    @Column(name = "problem_idx", nullable = false)
    private Integer problemIdx;

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
    public AuthLog(Integer accountIdx, Integer problemIdx, String authFlag, Boolean isSuccess) {
        this.accountIdx = accountIdx;
        this.problemIdx = problemIdx;
        this.authFlag = authFlag;
        this.isSuccess = isSuccess;
    }

    public Integer getProblemIdx() {
        return problemIdx;
    }

    public void setProblemIdx(Integer problemIdx) {
        this.problemIdx = problemIdx;
    }

    public Long getIdx() {
        return idx;
    }

    public void setIdx(Long idx) {
        this.idx = idx;
    }
}