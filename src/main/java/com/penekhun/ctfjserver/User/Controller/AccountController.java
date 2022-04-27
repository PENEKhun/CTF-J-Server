package com.penekhun.ctfjserver.User.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/account")
@Slf4j
public class AccountController {

    @PostMapping("/login")
    public void loginMap(){
        log.info("login");
    }

}
