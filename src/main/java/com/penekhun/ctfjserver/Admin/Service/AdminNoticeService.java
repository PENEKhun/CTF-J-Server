package com.penekhun.ctfjserver.Admin.Service;

import com.penekhun.ctfjserver.Admin.Dto.NoticeDto;
import com.penekhun.ctfjserver.Admin.Dto.NotificationDto;
import com.penekhun.ctfjserver.Config.Exception.CustomException;
import com.penekhun.ctfjserver.Config.Exception.ErrorCode;
import com.penekhun.ctfjserver.User.Entity.Notice;
import com.penekhun.ctfjserver.User.Repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@Secured("ROLE_ADMIN")
public class AdminNoticeService {
    private final NoticeRepository noticeRepository;
    private final AdminNotificationService adminNotificationService;

    /*


    view 는 일반 서비스에서!!

     */


    public void removeNotice(Integer noticeId){
       noticeRepository.deleteById(noticeId);
    }

    public void postNotice(NoticeDto.Req req){
        Notice notice = req.toEntity();
        noticeRepository.save(notice);
        if (Boolean.TRUE.equals(req.isNotificationMode())){
            NotificationDto.Req notificationDto = NotificationDto.Req.builder()
                            .title(req.getNotificationTitle())
                            .content(req.getNotificationContent())
                            .build();
            adminNotificationService.makeNotification(notificationDto);
        }
    }

    public void editNotice(final Integer id, NoticeDto.Req req){
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_INPUT_VALUE));
        notice.editWithDto(req);
        noticeRepository.save(notice);
        if (Boolean.TRUE.equals(req.isNotificationMode())){
            NotificationDto.Req notificationDto = NotificationDto.Req.builder()
                    .title(req.getNotificationTitle())
                    .content(req.getNotificationContent())
                    .build();
            adminNotificationService.makeNotification(notificationDto);
        }
    }

}
