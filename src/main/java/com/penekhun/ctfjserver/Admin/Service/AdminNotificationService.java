package com.penekhun.ctfjserver.Admin.Service;

import com.penekhun.ctfjserver.Admin.Dto.NotificationDto;
import com.penekhun.ctfjserver.Config.Exception.CustomException;
import com.penekhun.ctfjserver.Config.Exception.ErrorCode;
import com.penekhun.ctfjserver.Config.SecurityRole;
import com.penekhun.ctfjserver.User.Entity.Notification;
import com.penekhun.ctfjserver.User.Entity.NotificationDetail;
import com.penekhun.ctfjserver.User.Repository.AccountRepository;
import com.penekhun.ctfjserver.User.Repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminNotificationService {
    private final AccountRepository accountRepository;
    private final NotificationRepository notificationRepository;

    public void makeNotification(NotificationDto.Req req){
        List<Long> accountList =
                accountRepository.findAllIdByUserRole(SecurityRole.USER)
                        .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_DOESNT_EXIST));

        Notification notification = new Notification(req.getTitle(), req.getContent());
        accountList.forEach(accountId -> {
                NotificationDetail detail = new NotificationDetail(accountId);
                detail.setNotification(notification);
                }
        );
        notificationRepository.save(notification);
    }

    public void deleteNotification(Integer notificationId){
        Notification notification =
                notificationRepository.findById(notificationId)
                        .orElseThrow(() -> new CustomException(ErrorCode.INVALID_INPUT_VALUE));

        notificationRepository.delete(notification);



    }
}
