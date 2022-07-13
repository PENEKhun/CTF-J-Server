package com.penekhun.ctfjserver.Admin.Controller;

import com.penekhun.ctfjserver.Admin.Service.AdminAccountService;
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
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/account")
@Slf4j
@RequiredArgsConstructor
@Secured("ROLE_ADMIN")
@Tag(name = "admin.account", description = "[관리자 권한] 계정 관련 API 모음")
public class AdminAccountController {

    private final AdminAccountService adminAccountService;
    private final AccountService accountService;

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(security = { @SecurityRequirement(name = "bearer-key")},
            tags= {"admin.accounts"}, summary = "회원가입 API", description = "signup API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = AccountDto.Res.MyPage.class))),
            @ApiResponse(responseCode = "409", description = "입력한 정보와 중복되는 계정이 존재함", ref = "#/components/responses/ErrorCode.USERNAME_DUPLICATION")})
    public AccountDto.Res.Signup signupMapping(@Validated(AccountDto.Req.ValidationGroups.checkFullValid.class) AccountDto.Req.Signup signup) {
        return accountService.signup(signup);
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    @Operation(security = { @SecurityRequirement(name = "bearer-key")},
            tags= {"admin.accounts"}, summary = "회원정보를 불러오는 API", description = "get account API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = AccountDto.Res.MyPage.class)))})
    public AccountDto.Res.AccountList accountViewMapping(@PageableDefault(size = 30, sort = "id",  direction = Sort.Direction.DESC) Pageable pageable) {
        return accountService.getAllAccount(pageable);
    }

    @PatchMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(security = { @SecurityRequirement(name = "bearer-key")},
            tags= {"admin.accounts"}, summary = "부분 회원정보 수정", description = "editAccountPartly API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = Account.class))),
            @ApiResponse(responseCode = "403", description = "존재하지 않는 멤버", ref = "#/components/responses/ErrorCode.MEMBER_NOT_FOUND" ),
            @ApiResponse(responseCode = "409", description = "정보 수정시 중복되는 회원 값이 있을때", ref = "#/components/responses/ErrorCode.DUPLICATE_INFORMATION" )})
    public Account accountEditMapping(@PathVariable final Long id, AccountDto.Req.SignupWithoutValid editInfo) {
        try {
            return accountService.editAccountPartly(id, editInfo);
        } catch (DataIntegrityViolationException e){
            // mysql Unique 값 설정에 의해 생기는 오류
            throw new CustomException(ErrorCode.DUPLICATE_INFORMATION);
        }
    }

    @DeleteMapping("{id}")
    @Operation(security = { @SecurityRequirement(name = "bearer-key")},
            tags= {"admin.notice"}, summary = "계정을 삭제하는 API", description = "delete account API")
    public ResponseEntity<String> deleteNotice(@PathVariable Long id){
        if (id == null || id <= 0)
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
        adminAccountService.removeAccount(id);
        return ResponseEntity.noContent().build();
    }


}
