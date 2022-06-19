package com.penekhun.ctfjserver.User.Controller;

import com.penekhun.ctfjserver.Config.CurrentUserParameter;
import com.penekhun.ctfjserver.Config.Exception.CustomException;
import com.penekhun.ctfjserver.Config.Exception.ErrorCode;
import com.penekhun.ctfjserver.Config.Exception.ErrorResponse;
import com.penekhun.ctfjserver.User.Dto.AccountDto;
import com.penekhun.ctfjserver.User.Entity.Account;
import com.penekhun.ctfjserver.User.Service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/account")
@Slf4j
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    //todo : only access for admin
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(tags= {"accounts"}, summary = "회원가입 API", description = "signup API")
    @ApiResponse(responseCode = "409", description = "입력한 정보와 중복되는 계정이 존재함", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public AccountDto.Res.Signup signupMapping(@Validated AccountDto.Req.Signup signup) {
        return accountService.signup(signup);
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    @Operation(security = { @SecurityRequirement(name = "bearer-key")},
            tags= {"accounts"}, summary = "나의 회원정보 가져오는 API", description = "lookup my account")
    @ApiResponse(responseCode = "403", description = "잘못된 접근", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public AccountDto.Res.MyPage lookupMyAccountMapping(@CurrentUserParameter Account account){
        if (account == null)
            throw new CustomException(ErrorCode.HANDLE_ACCESS_DENIED);
        return accountService.getMyAccount(account);
    }

    @PatchMapping("")
    @ResponseStatus(HttpStatus.OK)
    @Operation(security = { @SecurityRequirement(name = "bearer-key")},
            tags= {"accounts"}, summary = "나의 회원정보의 일부를 수정하는 API", description = "edit my account partly")
    @ApiResponse(responseCode = "403", description = "잘못된 접근", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public AccountDto.Res.Signup editMyAccountPartlyMapping(@CurrentUserParameter Account account, AccountDto.Req.Signup signup){
        if (account == null)
            throw new CustomException(ErrorCode.HANDLE_ACCESS_DENIED);
        return accountService.editMyAccountPartly(signup);
    }




}
