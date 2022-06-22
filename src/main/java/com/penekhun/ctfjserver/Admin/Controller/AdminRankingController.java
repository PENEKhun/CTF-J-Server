package com.penekhun.ctfjserver.Admin.Controller;

import com.penekhun.ctfjserver.User.Service.RankingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/rank")
@Slf4j
@Secured("ROLE_ADMIN")
public class AdminRankingController {
    private final RankingService rankingService;

    @GetMapping("")
    @Operation(security = { @SecurityRequirement(name = "bearer-key")},
            tags= {"admin.rank"}, summary = "load rank history data API - 미완성", description = "랭크 히스토리 데이터를 불러옵니다.")
    public void getRankHisoryCached(){
    }

    @DeleteMapping("/history/{howMany}")
    @Operation(security = { @SecurityRequirement(name = "bearer-key")},
            tags= {"admin.rank"}, summary = "remove rank history data API - 미완성", description = "랭크 히스토리 데이터를 초기화 합니다.(/rank/history 정보를 초기화)")
    public void removeRankHisoryCached(){
    }

}