package com.penekhun.ctfjserver.User.Entity;

import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "NotificationDetail", schema = "ctf")
public class NotificationDetail {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "idx")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notification_idx")
    private Notification notification;

    @Column(name = "receiver_idx", nullable = false)
    private Long receiverIdx;

    @Column(name = "is_read", nullable = false)
    private boolean isRead;

    @LastModifiedDate
    @Column(name = "read_time", insertable = false)
    private Timestamp readTime;

    public NotificationDetail(Long receiverIdx) {
        this.receiverIdx = receiverIdx;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
        notification.addDetail(this);
    }

    public void setRead(){
        this.isRead = true;
    }


}