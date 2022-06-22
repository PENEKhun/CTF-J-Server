package com.penekhun.ctfjserver.Admin.Controller;

import com.penekhun.ctfjserver.User.Service.RankingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/log")
@Slf4j
@Secured("ROLE_ADMIN")
public class AdminLogController {
    private final RankingService rankingService;

    @GetMapping("flag/{currentPageNo}/{recordsPerPage}")
    @Operation(security = { @SecurityRequirement(name = "bearer-key")},
            tags= {"admin.log"}, summary = "플래그 인증 로그 API - 미완", description = "플래그 인증 시도한 로그를 보여주는 API")
    public void getAuthLog(){
    }


}