package com.penekhun.ctfjserver.User.Entity;

import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Table(name = "Notification", schema = "ctf")
public class Notification {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "idx")
    private Integer id;

    @CreatedDate
    @Column(name = "create_time", updatable = false)
    private LocalDateTime createTime;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @OneToMany(mappedBy = "notification",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NotificationDetail> to = new ArrayList<>();

    public Notification(String title, String content) {
        this.createTime = LocalDateTime.now();
        this.title = title;
        this.content = content;
    }

    public void addDetail(NotificationDetail notificationDetail) {
        to.add(notificationDetail);
    }
}