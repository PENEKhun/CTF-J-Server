package com.penekhun.ctfjserver.User.Controller;

import com.penekhun.ctfjserver.Config.CurrentUserParameter;
import com.penekhun.ctfjserver.User.Dto.AccountDto;
import com.penekhun.ctfjserver.User.Entity.Account;
import com.penekhun.ctfjserver.User.Service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
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
    public AccountDto.Res.Signup signupMapping(@Validated AccountDto.Req.Signup signup) {
        return accountService.signup(signup);
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    @Operation(tags= {"accounts"}, summary = "나의 회원정보 가져오는 API", description = "lookup my account")
    public AccountDto.Res.MyPage lookupMyAccountMapping(@CurrentUserParameter Account account){
        return accountService.getMyAccount(account);
    }

    @PatchMapping("")
    @ResponseStatus(HttpStatus.OK)
    @Operation(tags= {"accounts"}, summary = "나의 회원정보의 일부를 수정하는 API", description = "edit my account partly")
    public AccountDto.Res.Signup editMyAccountPartlyMapping(@CurrentUserParameter Account account, AccountDto.Req.Signup signup){
        return accountService.editMyAccountPartly(signup);
    }




}
