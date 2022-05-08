package com.penekhun.ctfjserver.User.Controller;

import com.penekhun.ctfjserver.Config.Jwt.JwtFilter;
import com.penekhun.ctfjserver.Config.Jwt.TokenProvider;
import com.penekhun.ctfjserver.User.Dto.AccountDto;
import com.penekhun.ctfjserver.User.Dto.TokenDto;
import com.penekhun.ctfjserver.User.Service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/")
@Slf4j
@RequiredArgsConstructor
public class LoginOutController {

    private final AccountService accountService;

    @PostMapping("login")
    public ResponseEntity loginMap
            (@RequestParam String username,
             @RequestParam String password){

        return accountService.login(username, password);
    }

    @GetMapping("logout")
    public ResponseEntity logoutMap(){
        return ResponseEntity.ok("logout");
    }

    @PostMapping("tempSignup")
    public ResponseEntity<String> postMapping(@Validated AccountDto.Req.Signup signup){
        //post Account == Signup
        log.info("post account");
        accountService.signup(signup);

        return ResponseEntity.ok().body("asd");
    }

}
