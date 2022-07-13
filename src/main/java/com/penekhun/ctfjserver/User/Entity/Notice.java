package com.penekhun.ctfjserver.User.Entity;

import com.penekhun.ctfjserver.Admin.Dto.NoticeDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "Notice", schema = "ctf")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Getter
public class Notice {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "idx", nullable = false)
    private Integer id;

    @Column(name = "title", nullable = false, length = 45)
    private String title;

    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    @CreatedDate
    @Column(name = "create_time", nullable = false, updatable = false)
    private Timestamp createTime;

    @LastModifiedDate
    @Column(name = "update_time", updatable = false)
    private Timestamp updateTime;


    public Notice(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void editWithDto(NoticeDto.Req req){
        if (!req.getTitle().isBlank())
            this.title = req.getTitle();
        if (!req.getContent().isBlank())
            this.content = req.getContent();
    }
}