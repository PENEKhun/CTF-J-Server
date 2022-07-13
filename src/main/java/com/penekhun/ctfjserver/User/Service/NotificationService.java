package com.penekhun.ctfjserver.User.Service;

import com.penekhun.ctfjserver.Config.Exception.CustomException;
import com.penekhun.ctfjserver.Config.Exception.ErrorCode;
import com.penekhun.ctfjserver.User.Entity.Account;
import com.penekhun.ctfjserver.User.Entity.Notification;
import com.penekhun.ctfjserver.User.Entity.NotificationDetail;
import com.penekhun.ctfjserver.User.Repository.NotificationDetailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class NotificationService {
    private final NotificationDetailRepository notificationDetailRepository;

    public void readNotification(Account account, Integer notificationId){
        NotificationDetail notificationDetail = notificationDetailRepository.findFirstByNotificationIdAndReceiverIdx(notificationId, account.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_INPUT_VALUE));

        notificationDetail.setRead();
        notificationDetailRepository.save(notificationDetail);
    }

    @Transactional(readOnly=true)
    public List<Notification> fetchNotification(Account account){
        return notificationDetailRepository.findNotReadNotification(account.getId());
    }

}
