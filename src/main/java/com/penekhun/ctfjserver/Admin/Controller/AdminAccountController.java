package com.penekhun.ctfjserver.Admin.Controller;

import com.penekhun.ctfjserver.User.Dto.AccountDto;
import com.penekhun.ctfjserver.User.Service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/account")
@Slf4j
@RequiredArgsConstructor
@Secured("ROLE_ADMIN")
@Tag(name = "admin.account", description = "[관리자 권한] 계정 관련 API 모음")
public class AdminAccountController {

    private final AccountService accountService;

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(security = { @SecurityRequirement(name = "bearer-key")},
            tags= {"admin.accounts"}, summary = "회원가입 API", description = "signup API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = AccountDto.Res.MyPage.class))),
            @ApiResponse(responseCode = "409", description = "입력한 정보와 중복되는 계정이 존재함", ref = "#/components/responses/ErrorCode.USERNAME_DUPLICATION")})
    public AccountDto.Res.Signup signupMapping(@Validated AccountDto.Req.Signup signup) {
        return accountService.signup(signup);
    }


}
