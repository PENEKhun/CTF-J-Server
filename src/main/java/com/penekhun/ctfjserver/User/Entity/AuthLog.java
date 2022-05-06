package com.penekhun.ctfjserver.User.Entity;

import org.springframework.data.jpa.domain.AbstractAuditable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "AuthLog")
public class AuthLog extends AbstractAuditable {
    @Id
    @Column(name = "idx", nullable = false)
    private Long id1;

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

    public Long getId1() {
        return id1;
    }

    public void setId1(Long id1) {
        this.id1 = id1;
    }
}