package com.penekhun.ctfjserver.User.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1/file")
@Controller
public class ProblemFileController {

    @PostMapping("")
    public void upload(){

    }

    @GetMapping
    public void download(){

    }
}
