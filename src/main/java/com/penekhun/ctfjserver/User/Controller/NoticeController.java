package com.penekhun.ctfjserver.User.Controller;

import com.penekhun.ctfjserver.User.Entity.Notice;
import com.penekhun.ctfjserver.User.Service.NoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class NoticeController {
    private final NoticeService noticeService;

    @GetMapping({"/api/v1/admin/notice", "/api/v1/notice"})
    @Operation(security = { @SecurityRequirement(name = "bearer-key")},
            tags= {"admin.notice"}, summary = "공지 가져오는 API - 미완", description = "get notice")
    public List<Notice> getNoticeAllMapping(){
        return noticeService.getNoticeAll();
    }

}
