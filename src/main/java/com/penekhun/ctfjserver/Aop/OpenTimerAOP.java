package com.penekhun.ctfjserver.Aop;

import com.penekhun.ctfjserver.Config.CurrentUser;
import com.penekhun.ctfjserver.Config.Exception.CustomException;
import com.penekhun.ctfjserver.Config.Exception.ErrorCode;
import com.penekhun.ctfjserver.User.Controller.LoginOutController;
import com.penekhun.ctfjserver.User.Dto.TokenDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
@Getter
public class OpenTimerAOP {


    @Value("${server.enable-open-timer}") boolean enableTimer;
    @Value("${server.open-time}") String openTimeString;
    @Value("${server.open-time-format}") String openTimeFormat;
    @Value("${server.end-time}") String endTimeString;
    @Value("${server.end-time-format}") String endTimeFormat;
    @Value("${server.time-zone}") String timeZone;
    private final CurrentUser currentUser;
    private final LoginOutController loginOutController;

    // AccountService 클래스에 login 메서드에 적용
    @AfterReturning(
            pointcut = "execution(* com.penekhun.ctfjserver.User.Service.AccountService.login(..))",
            returning = "results"
    )
    public void checkSiteIsOpen(TokenDto results) {

        try {

            if (enableTimer){
                SimpleDateFormat sdf = new SimpleDateFormat(openTimeFormat);
                sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
                Date openTimeDate = sdf.parse(openTimeString);
                Date endTimeDate = sdf.parse(endTimeString);
                String nowTimeDate = sdf.format(new Date());

                if (currentUser.isAdmin())
                    return;

                if (!(openTimeDate.before(sdf.parse(nowTimeDate)))) {
//                    open
                    throw new CustomException(ErrorCode.SERVER_NOT_OPEN);
                } else if ((endTimeDate.before(sdf.parse(nowTimeDate))))
//                    close
                    throw new CustomException(ErrorCode.SERVER_NOT_OPEN);
            }
        } catch (NullPointerException e){
            throw new CustomException(ErrorCode.TIME_ZONE_ERROR);
        } catch (ParseException e){
            throw new CustomException(ErrorCode.OPEN_TIME_ERROR);
        }
    }

}
