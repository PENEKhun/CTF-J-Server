package com.penekhun.ctfjserver.Admin.Controller;

import com.penekhun.ctfjserver.Admin.Dto.LogDto;
import com.penekhun.ctfjserver.Admin.Service.AdminLogService;
import com.penekhun.ctfjserver.Config.Exception.CustomException;
import com.penekhun.ctfjserver.Config.Exception.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
    private final AdminLogService adminLogService;

    @GetMapping("")
    @Operation(security = { @SecurityRequirement(name = "bearer-key")},
            tags= {"admin.log"}, summary = "로그 API", description = "로그를 보여주는 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = LogDto.Res.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 입력 값", ref = "#/components/responses/ErrorCode.INVALID_INPUT_VALUE")})
    public LogDto.Res getLogMapping(final String logType, @PageableDefault(size = 30, sort = "id",  direction = Sort.Direction.DESC) Pageable pageable){

        if (logType.equalsIgnoreCase("FLAG")) {
            return adminLogService.getAuthLog(pageable);
        } else if (logType.equalsIgnoreCase("LOG")) {
            return adminLogService.getLog(pageable);
        } else
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
    }



}