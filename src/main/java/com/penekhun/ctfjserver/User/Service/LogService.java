package com.penekhun.ctfjserver.User.Service;

import com.penekhun.ctfjserver.Config.CurrentUser;
import com.penekhun.ctfjserver.User.Entity.AuthLog;
import com.penekhun.ctfjserver.User.Repository.AuthLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class LogService {

    private final AuthLogRepository authLogRepository;
    private final CurrentUser currentUser;

    public void problemServiceLog(){

    }

    public void uploadProblemLog(){

    }

    public void uploadFileLog(){

    }


    @Transactional
    public void authProblemLog(Integer problemId, String authFlag, boolean isSuccess){
        AuthLog authLog = AuthLog.builder().
                problemIdx(problemId).
                authFlag(authFlag).
                isSuccess(isSuccess).
                accountIdx(currentUser.getUID())
                .build();
        authLogRepository.save(authLog);
    }


}
