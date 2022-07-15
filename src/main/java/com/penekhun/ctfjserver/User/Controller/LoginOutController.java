package com.penekhun.ctfjserver.User.Controller;

import com.penekhun.ctfjserver.User.Dto.AccountDto;
import com.penekhun.ctfjserver.User.Dto.TokenDto;
import com.penekhun.ctfjserver.User.Service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.penekhun.ctfjserver.Config.Jwt.JwtFilter.AUTHORIZATION_HEADER;
import static com.penekhun.ctfjserver.Config.Jwt.JwtFilter.REFRESH_TOKEN_HEADER;

@RestController
@RequestMapping("/api/v1/")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "accounts", description = "계정(로그인)관련 API 모음")
public class LoginOutController {

    private final AccountService accountService;

    @Operation(security = { @SecurityRequirement(name = "bearer-key")},
            tags= {"accounts", "admin.accounts"}, summary = "로그인 API", description = "login API")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "로그인 성공"),
        @ApiResponse(responseCode = "400", description = "유효성 검증 오류", ref = "#/components/responses/ErrorCode.INVALID_INPUT_VALUE"),
        @ApiResponse(responseCode = "403", description = "존재하는 계정 없음", ref = "#/components/responses/ErrorCode.MEMBER_NOT_FOUND")})
    @PostMapping("login")
    @ResponseStatus(HttpStatus.OK)
    public TokenDto loginMap(AccountDto.Req.Login login){
        return accountService.login(login);
    }

    @Operation(tags= {"accounts", "admin.accounts"}, summary = "토큰 재발급 API", description = "token reissue API")
    @PostMapping("/reissue")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses({
        @ApiResponse(responseCode = "501", description = "존재하지 않는 토큰", ref = "#/components/responses/ErrorCode.DOESNT_EXIST_TOKEN"),
        @ApiResponse(responseCode = "403", description = "잘못된 접근", ref = "#/components/responses/ErrorCode.HANDLE_ACCESS_DENIED"),
        @ApiResponse(responseCode = "500", description = "알수없는 오류", ref= "#/components/responses/ErrorCode.UNCHECKED_ERROR")})
    public ResponseEntity reissueMap(@RequestHeader(AUTHORIZATION_HEADER) String accessToken,
                                     @RequestHeader(REFRESH_TOKEN_HEADER) String refreshToken){
        TokenDto tokenDto = accountService.reissue(accessToken, refreshToken);
        return new ResponseEntity<>(tokenDto, HttpStatus.OK);
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key")},
            tags= {"accounts", "admin.accounts"}, summary = "로그아웃 API", description = "logout API")
    @GetMapping("logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity logoutMap(){
        return accountService.logout();
    }

    @Operation(tags= {"accounts"}, summary = "[테스트용] 회원가입 API", description = "login API for Test")
    @PostMapping("tempSignup")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses({
            @ApiResponse(responseCode = "409", description = "입력한 정보와 중복되는 계정이 존재함", ref = "#/components/responses/ErrorCode.USERNAME_DUPLICATION")})
    public AccountDto.Res.Signup postMapping(@Validated AccountDto.Req.Signup signup){
        //post Account == Signup
        log.info("post account");

        return accountService.signup(signup);
    }

}
