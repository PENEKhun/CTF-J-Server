package com.penekhun.ctfjserver.Admin.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/notice")
@Slf4j
@Secured("ROLE_ADMIN")
public class AdminNoticeController {


    @GetMapping("")
    @Operation(security = { @SecurityRequirement(name = "bearer-key")},
            tags= {"admin.notice"}, summary = "공지 가져오는 API - 미완", description = "get notice")
    public void getNotice(){

    }

    @PostMapping("")
    @Operation(security = { @SecurityRequirement(name = "bearer-key")},
            tags= {"admin.notice"}, summary = "공지 작성하는 API - 미완", description = "post notice")
    public void postNotice(){

    }

    @DeleteMapping("")
    @Operation(security = { @SecurityRequirement(name = "bearer-key")},
            tags= {"admin.notice"}, summary = "delete notice API - 미완", description = "delete notice API")
    public void deleteNotice(){

    }


}