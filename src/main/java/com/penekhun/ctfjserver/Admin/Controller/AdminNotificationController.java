package com.penekhun.ctfjserver.Admin.Controller;

import com.penekhun.ctfjserver.Admin.Dto.NotificationDto;
import com.penekhun.ctfjserver.Admin.Service.AdminNotificationService;
import com.penekhun.ctfjserver.Config.Exception.CustomException;
import com.penekhun.ctfjserver.Config.Exception.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/notification")
@Slf4j
@Secured("ROLE_ADMIN")
public class AdminNotificationController {
    private final AdminNotificationService adminNotificationService;

    @PostMapping("")
    @Operation(security = { @SecurityRequirement(name = "bearer-key")},
            tags= {"admin.notification"}, summary = "알림 생성 API", description = "make notification")
    public ResponseEntity<String> makeNotificationMapping(NotificationDto.Req req){
//        if (req.getNotificationMode().equals(NotificationDto.NotificationMode.ALL))
        adminNotificationService.makeNotification(req);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{id}")
    @Operation(security = { @SecurityRequirement(name = "bearer-key")},
            tags= {"admin.notice"}, summary = "알림 삭제 API", description = "delete notification")
    public ResponseEntity<String> deleteNotification(@PathVariable Integer id){
        if (id == null || id <= 0)
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
        adminNotificationService.deleteNotification(id);

        return ResponseEntity.noContent().build();
    }

}
