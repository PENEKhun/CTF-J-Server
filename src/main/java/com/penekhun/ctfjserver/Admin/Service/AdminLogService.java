package com.penekhun.ctfjserver.Admin.Service;

import com.penekhun.ctfjserver.Admin.Dto.LogDto;
import com.penekhun.ctfjserver.User.Entity.AuthLog;
import com.penekhun.ctfjserver.User.Entity.LogStore;
import com.penekhun.ctfjserver.User.Repository.AuthLogRepository;
import com.penekhun.ctfjserver.User.Repository.LogStoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AdminLogService {
    private final LogStoreRepository logStoreRepository;
    private final AuthLogRepository authLogRepository;

    public LogDto.Res getAuthLog(LogDto.Req req){
        Sort sort = Sort.by(Sort.Order.desc("idx"));
        Pageable pageable = PageRequest.ofSize(req.getAmount())
                .withPage(req.getPageNum())
                .withSort(sort);

        Page<AuthLog> authLogPage = authLogRepository.findAll(pageable);
        List<AuthLog> authLogList = authLogPage.getContent();

        LogDto.Res response = new LogDto.Res();
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


    public LogDto.Res getLog(LogDto.Req req){
        Sort sort = Sort.by(Sort.Order.desc("idx"));
        Pageable pageable = PageRequest.ofSize(req.getAmount())
                .withPage(req.getPageNum())
                .withSort(sort);

        Page<LogStore> logPage = logStoreRepository.findAll(pageable);
        List<LogStore> logList = logPage.getContent();

        LogDto.Res response = new LogDto.Res();
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
