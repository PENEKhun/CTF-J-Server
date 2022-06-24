package com.penekhun.ctfjserver.User.Entity;

import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@NoArgsConstructor
@Table(name = "AuthLog", schema = "ctf")
@EntityListeners(AuditingEntityListener.class)
public class AuthLog {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "idx", nullable = false)
    private Long idx;

    @ManyToOne @JoinColumn(name = "PROBLEM_IDX")
    //@Column(name = "problem_idx", nullable = false)
    private Problem problem;

    @ManyToOne @JoinColumn(name = "ACCOUNT_IDX", insertable = false, updatable = false)
    private Account solver;

    @Column(name = "account_idx", nullable = false)
    private Integer accountIdx;

    @Column(name = "auth_flag", nullable = false, length = 150)
    private String authFlag;

    @Column(name = "is_success", nullable = false)
    private boolean isSuccess = false;

    @CreatedDate
    @Column(name = "auth_time", nullable = false, updatable = false)
    private Timestamp authAt;

    public Long getIdx() {
        return idx;
    }

    public String getUsername() {
        if (solver != null)
            return solver.getUsername();
        else return "";
    }

    public String getNickname(){
        if (solver != null)
            return solver.getNickname();
        else return "";
    }
    public Integer getAccountIdx() {
        return accountIdx;
    }

    public Timestamp getAuthAt() {
        return authAt;
    }

    public boolean isSuccess() {
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
    public AuthLog(Problem problem, Integer accountIdx, String authFlag, boolean isSuccess) {
        this.problem = problem;
        this.accountIdx = accountIdx;
        this.authFlag = authFlag;
        this.isSuccess = isSuccess;
    }

}