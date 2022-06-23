package com.penekhun.ctfjserver.Aop;

import com.penekhun.ctfjserver.User.Service.LogService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
@Getter
public class AdminLogAOP {

    private final LogService logService;

    // /api/v1/admin 접근시 로그
    @AfterReturning(
            pointcut = "execution(* com.penekhun.ctfjserver.Admin.Controller.*.*(..))"
    )
    public void afterAccessAdminPath(JoinPoint joinPoint) {
        logService.logAdminAccess(joinPoint.toShortString());
    }

}
