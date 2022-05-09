package com.penekhun.ctfjserver.User.Controller;

import com.penekhun.ctfjserver.User.Dto.AccountDto;
import com.penekhun.ctfjserver.User.Service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/account")
@Slf4j
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("")
    public ResponseEntity postMapping(@RequestBody @Validated AccountDto.Req.Signup signup){
        //post Account == Signup
        log.info("post account");
        accountService.signup(signup);



        return ResponseEntity.ok("post Account");
    }



}
