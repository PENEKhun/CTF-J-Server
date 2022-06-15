package com.penekhun.ctfjserver.User.Controller;

import com.penekhun.ctfjserver.Aop.OpenTimerAOP;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/api/config")
@Slf4j
@RequiredArgsConstructor
public class ConfigController {
    private final OpenTimerAOP openTimerAOP;

    @GetMapping("")
    public Collection<String> configMapping(){
        //todo: openTime, endTime Config Data 출력
        //openTimerAOP.getOpenTimeFormat()
        return null;
    }

}
