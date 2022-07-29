package com.penekhun.ctfjserver.User.Controller;

import com.penekhun.ctfjserver.Config.CurrentUserParameter;
import com.penekhun.ctfjserver.Config.Exception.CustomException;
import com.penekhun.ctfjserver.Config.Exception.ErrorCode;
import com.penekhun.ctfjserver.User.Dto.AccountDto;
import com.penekhun.ctfjserver.User.Entity.Account;
import com.penekhun.ctfjserver.User.Service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/account")
@Slf4j
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    @Operation(security = { @SecurityRequirement(name = "bearer-key")},
            tags= {"accounts"}, summary = "나의 회원정보 가져오는 API", description = "lookup my account")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = AccountDto.Res.MyPage.class))),
            @ApiResponse(responseCode = "403", description = "잘못된 접근", ref = "#/components/responses/ErrorCode.HANDLE_ACCESS_DENIED"),
            @ApiResponse(responseCode = "500", description = "알수없는 오류", ref= "#/components/responses/ErrorCode.UNCHECKED_ERROR")})
    public AccountDto.Res.MyPage lookupMyAccountMapping(@CurrentUserParameter Account account){
        if (account == null)
            throw new CustomException(ErrorCode.HANDLE_ACCESS_DENIED);
        return accountService.getMyAccount(account);
    }

    @PutMapping("/pw")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(security = { @SecurityRequirement(name = "bearer-key")},
            tags= {"accounts"}, summary = "나의 비밀번호를 변경하는 API", description = "edit my Account's password")
    @ApiResponses({
            @ApiResponse(responseCode = "203", description = "성공", content = @Content(schema = @Schema(implementation = AccountDto.Res.MyPage.class))),
            @ApiResponse(responseCode = "403", description = "잘못된 접근", ref = "#/components/responses/ErrorCode.HANDLE_ACCESS_DENIED"),
            @ApiResponse(responseCode = "500", description = "알수없는 오류", ref= "#/components/responses/ErrorCode.MEMBER_NOT_FOUND"),
            @ApiResponse(responseCode = "409", description = "기존 비밀번호가 맞지 않음", ref= "#/components/responses/ErrorCode.PASSWORD_NOT_MATCH")})
    public ResponseEntity<String> editMyPasswordMapping(@CurrentUserParameter Account account, @Valid AccountDto.Req.ChangePassword changePassword){
        if (account == null)
            throw new CustomException(ErrorCode.HANDLE_ACCESS_DENIED);
        accountService.editMyPassword(account.getId(), changePassword.getNewPassword(), changePassword.getOldPassword());
        return ResponseEntity.noContent().build();
    }

//    @PatchMapping("")
//    @ResponseStatus(HttpStatus.OK)
//    @Operation(security = { @SecurityRequirement(name = "bearer-key")},
//            tags= {"accounts"}, summary = "나의 회원정보의 일부를 수정하는 API", description = "edit my account partly")
//    @ApiResponse(responseCode = "403", description = "잘못된 접근", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
//    public AccountDto.Res.Signup editMyAccountPartlyMapping(@CurrentUserParameter Account account, AccountDto.Req.Signup signup){
//        if (account == null)
//            throw new CustomException(ErrorCode.HANDLE_ACCESS_DENIED);
//        return accountService.editMyAccountPartly(signup);
//    }




}
