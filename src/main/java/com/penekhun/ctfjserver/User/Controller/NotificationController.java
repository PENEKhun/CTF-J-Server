package com.penekhun.ctfjserver.User.Controller;

import com.penekhun.ctfjserver.Config.CurrentUserParameter;
import com.penekhun.ctfjserver.Config.Exception.CustomException;
import com.penekhun.ctfjserver.Config.Exception.ErrorCode;
import com.penekhun.ctfjserver.User.Entity.Account;
import com.penekhun.ctfjserver.User.Entity.Notification;
import com.penekhun.ctfjserver.User.Service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notification")
@Slf4j
public class NotificationController {
    private final NotificationService notificationService;


    @GetMapping("")
    @Operation(security = { @SecurityRequirement(name = "bearer-key")},
            tags= {"user.notification"}, summary = "읽지 않은 알림을 가져오는 API", description = "get un-Read Notification")
    public List<Notification> fetchNotificationMap(@CurrentUserParameter Account account){
        return notificationService.fetchNotification(account);
    }


    @GetMapping("{notificationId}")
    @Operation(security = { @SecurityRequirement(name = "bearer-key")},
            tags= {"user.notification"}, summary = "알림 읽음 처리 API", description = "read Notification")
    public ResponseEntity<String> readNotificationMap(@CurrentUserParameter Account account, @PathVariable Integer notificationId){
        if (notificationId == null || notificationId <= 0)
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);

        notificationService.readNotification(account, notificationId);
        return ResponseEntity.ok().build();
    }

}
