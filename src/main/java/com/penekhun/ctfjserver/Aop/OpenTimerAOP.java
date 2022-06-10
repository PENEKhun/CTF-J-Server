package com.penekhun.ctfjserver.Aop;

import com.penekhun.ctfjserver.Config.CurrentUser;
import com.penekhun.ctfjserver.Config.Exception.CustomException;
import com.penekhun.ctfjserver.Config.Exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class OpenTimerAOP {


    @Value("${server.enable-open-timer}") boolean enableTimer;
    @Value("${server.open-time}") String openTime;
    @Value("${server.open-time-format}") String openTimeFormat;
    @Value("${server.time-zone}") String timeZone;
    private final CurrentUser currentUser;

    // User/controller 이하 패키지의 모든 클래스 이하 모든 메서드에 적용
    @Pointcut("execution(* com.penekhun.ctfjserver.User.Controller..*(..))")
    private void accessController(){}

    @Before("accessController()")
    public void checkSiteIsOpen(JoinPoint joinPoint) {


        Method method = getMethod(joinPoint);

        if (enableTimer){
            SimpleDateFormat sdf = new SimpleDateFormat(openTimeFormat);

            try {
                sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
            } catch (NullPointerException e){
                throw new CustomException(ErrorCode.TIME_ZONE_ERROR);
            }

            try {
                Date openTimeDate = sdf.parse(openTime);
                String nowTimeDate = sdf.format(new Date());

                if (!method.getName().startsWith("login") && !currentUser.isAdmin()
                    && !(openTimeDate.before(sdf.parse(nowTimeDate)))) {
                    log.info("server not open");
                    throw new CustomException(ErrorCode.SERVER_NOT_OPEN);
                }

            } catch (ParseException e){
                throw new CustomException(ErrorCode.OPEN_TIME_ERROR);
            }
        }
    }

    private Method getMethod(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getMethod();
    }

}
