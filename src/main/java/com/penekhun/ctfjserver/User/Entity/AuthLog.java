package com.penekhun.ctfjserver.User.Entity;

import org.springframework.data.jpa.domain.AbstractAuditable;

import javax.persistence.*;

@Entity
@Table(name = "AuthLog", schema = "ctf")
public class AuthLog {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "idx", nullable = false)
    private Long idx;

    @Column(name = "problem_idx", nullable = false)
    private Integer problemIdx;

    @Column(name = "auth_flag", nullable = false, length = 100)
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