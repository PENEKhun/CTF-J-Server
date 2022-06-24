package com.penekhun.ctfjserver.User.Entity;

import lombok.Builder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "LogStore", schema = "ctf")
public class LogStore {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "idx", nullable = false)
    private Long id;

    @ManyToOne @JoinColumn(name = "ACCOUNT_IDX", insertable = false, updatable = false)
    private Account user;

    @Column(name = "account_idx", nullable = false)
    private Integer accountIdx;

    @Column(name = "action", nullable = false, length = 45)
    private String action;

    @Column(name = "detail", nullable = false, length = 100)
    private String detail;

    @CreatedDate
    @Column(name = "time", nullable = false, updatable = false)
    private Timestamp time;

    @Builder
    public LogStore(Integer memberIdx, String action, String detail) {
        this.accountIdx = memberIdx;
        this.action = action;
        this.detail = detail;
    }

}
