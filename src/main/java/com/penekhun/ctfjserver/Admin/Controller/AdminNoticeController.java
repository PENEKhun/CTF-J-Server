package com.penekhun.ctfjserver.Admin.Controller;

import com.penekhun.ctfjserver.Admin.Dto.NoticeDto;
import com.penekhun.ctfjserver.Admin.Service.AdminNoticeService;
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
@RequestMapping("/api/v1/admin/notice")
@Slf4j
@Secured("ROLE_ADMIN")
public class AdminNoticeController {

    private final AdminNoticeService adminNoticeService;

    @PostMapping("")
    @Operation(security = { @SecurityRequirement(name = "bearer-key")},
            tags= {"admin.notice"}, summary = "공지 작성하는 API", description = "post notice")
    public ResponseEntity<String> postNotice(NoticeDto.Req req){
        //valid
        adminNoticeService.postNotice(req);
        return ResponseEntity.ok().build();
    }

    @PostMapping("{id}")
    @Operation(security = { @SecurityRequirement(name = "bearer-key")},
            tags= {"admin.notice"}, summary = "공지 수정하는 API", description = "post notice")
    public ResponseEntity<String> editNotice(@PathVariable Integer id, NoticeDto.Req req){
        if (id == null || id <= 0)
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
        adminNoticeService.editNotice(id, req);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{id}")
    @Operation(security = { @SecurityRequirement(name = "bearer-key")},
            tags= {"admin.notice"}, summary = "delete notice API", description = "delete notice API")
    public ResponseEntity<String> deleteNotice(@PathVariable Integer id){
        if (id == null || id <= 0)
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
        adminNoticeService.removeNotice(id);
        return ResponseEntity.noContent().build();
    }


}