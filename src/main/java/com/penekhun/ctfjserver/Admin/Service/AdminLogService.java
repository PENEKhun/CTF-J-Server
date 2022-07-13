package com.penekhun.ctfjserver.Admin.Service;

import com.penekhun.ctfjserver.Admin.Dto.LogDto;
import com.penekhun.ctfjserver.User.Entity.AuthLog;
import com.penekhun.ctfjserver.User.Entity.LogStore;
import com.penekhun.ctfjserver.User.Repository.AuthLogRepository;
import com.penekhun.ctfjserver.User.Repository.LogStoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
@Secured("ROLE_ADMIN")
public class AdminLogService {
    private final LogStoreRepository logStoreRepository;
    private final AuthLogRepository authLogRepository;

    @Transactional(readOnly=true)
    public LogDto.Res getAuthLog(Pageable pageable){
        Page<AuthLog> authLogPage = authLogRepository.findAll(pageable);
        List<AuthLog> authLogList = authLogPage.getContent();

        LogDto.Res response = new LogDto.Res(authLogPage.getTotalPages(), authLogPage.getTotalElements());
        for (AuthLog authLog : authLogList) {
            LogDto.Item res = LogDto.Item.builder()
                    .id(authLog.getIdx())
                    .accountIdx(authLog.getAccountIdx())
                    .action(String.format("%s(%s) tried flag auth", authLog.getNickname(), authLog.getUsername()))
                    .detail(String.format("input : %s and is_success=%b", authLog.getAuthFlag(), authLog.isSuccess()))
                    .time(authLog.getAuthAt())
                    .build();
            response.addLog(res);
        }
        return response;
    }

    @Transactional(readOnly=true)
    public LogDto.Res getLog(Pageable pageable){
        Page<LogStore> logPage = logStoreRepository.findAll(pageable);
        List<LogStore> logList = logPage.getContent();

        LogDto.Res response = new LogDto.Res(logPage.getTotalPages(), logPage.getTotalElements());
        for (LogStore log : logList) {
            LogDto.Item res = LogDto.Item.builder()
                    .id(log.getId())
                    .accountIdx(log.getAccountIdx())
                    .action(String.format("%s(%s) access admin", log.getNickname(), log.getUsername()))
                    .detail(log.getDetail())
                    .time(log.getTime())
                    .build();
            response.addLog(res);
        }
        return response;
    }

}
