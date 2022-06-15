package com.penekhun.ctfjserver.User.Controller;

import com.penekhun.ctfjserver.User.Dto.AccountDto;
import com.penekhun.ctfjserver.User.Dto.TokenDto;
import com.penekhun.ctfjserver.User.Service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @Operation(tags= {"accounts"}, summary = "로그인 API", description = "login API")
    @ApiResponse(responseCode = "200", description = "OK !!")
    @PostMapping("login")
    public TokenDto loginMap(AccountDto.Req.Login login){
        return accountService.login(login);
    }

    @Operation(tags= {"accounts"}, summary = "토큰 재발급 API", description = "token reissue API")
    @PostMapping("/reissue")
    public ResponseEntity reissueMap(@RequestHeader(AUTHORIZATION_HEADER) String accessToken,
                                     @RequestHeader(REFRESH_TOKEN_HEADER) String refreshToken){
        return accountService.reissue(accessToken, refreshToken);
    }

    @Operation(tags= {"accounts"}, summary = "로그아웃 API", description = "logout API")
    @GetMapping("logout")
    public ResponseEntity logoutMap(){
        return ResponseEntity.ok("logout");
    }

    @Operation(tags= {"accounts"}, summary = "[테스트용] 회원가입 API", description = "login API for Test")
    @PostMapping("tempSignup")
    public AccountDto.Res.Signup postMapping(@Validated AccountDto.Req.Signup signup){
        //post Account == Signup
        log.info("post account");

        return accountService.signup(signup);
    }

}
