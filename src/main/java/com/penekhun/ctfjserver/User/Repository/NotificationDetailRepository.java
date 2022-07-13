package com.penekhun.ctfjserver.User.Repository;

import com.penekhun.ctfjserver.User.Entity.Notification;
import com.penekhun.ctfjserver.User.Entity.NotificationDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface NotificationDetailRepository extends JpaRepository<NotificationDetail, Integer> {
    Optional<NotificationDetail> findFirstByNotificationIdAndReceiverIdx(Integer notificationId, Long receiverIdx);

    @Query("SELECT detail.notification FROM NotificationDetail detail " +
            "WHERE detail.receiverIdx = :receiverIdx AND detail.isRead = False")
    List<Notification> findNotReadNotification(Long receiverIdx);
}
