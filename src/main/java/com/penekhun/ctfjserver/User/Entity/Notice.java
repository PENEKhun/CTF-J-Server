package com.penekhun.ctfjserver.User.Entity;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "Notice")
public class Notice {
    @Id
    @Column(name = "idx", nullable = false)
    private Integer id;

    @Column(name = "title", nullable = false, length = 45)
    private String title;

    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "create_time", nullable = false)
    private Instant createTime;

    @Column(name = "update_time")
    private Instant updateTime;

    public Instant getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Instant updateTime) {
        this.updateTime = updateTime;
    }

    public Instant getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Instant createTime) {
        this.createTime = createTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}