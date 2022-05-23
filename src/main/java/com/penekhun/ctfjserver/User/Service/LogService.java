package com.penekhun.ctfjserver.User.Service;

import com.penekhun.ctfjserver.Config.CurrentUser;
import com.penekhun.ctfjserver.Config.Exception.CustomException;
import com.penekhun.ctfjserver.Config.Exception.ErrorCode;
import com.penekhun.ctfjserver.User.Entity.AuthLog;
import com.penekhun.ctfjserver.User.Entity.LogStore;
import com.penekhun.ctfjserver.User.Entity.Problem;
import com.penekhun.ctfjserver.User.Entity.ProblemFile;
import com.penekhun.ctfjserver.User.Repository.AuthLogRepository;
import com.penekhun.ctfjserver.User.Repository.LogStoreRepository;
import com.penekhun.ctfjserver.User.Repository.ProblemFileRepository;
import com.penekhun.ctfjserver.User.Repository.ProblemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class LogService {

    private final AuthLogRepository authLogRepository;
    private final LogStoreRepository logStoreRepository;
    private final ProblemFileRepository problemFileRepository;
    private final ProblemRepository problemRepository;
    private final CurrentUser currentUser;

    public void uploadProblemLog(Problem problem){
        logging("uploadProblem", String.format("problem name : %s", problem.getTitle()));
    }

    public void uploadFileLog(MultipartFile file, String fileName){
        logging("uploadProblemFile", String.format("fileName %s", fileName));
        ProblemFile problemFile = ProblemFile.builder().
                fileName(fileName).
                originalFileName(file.getOriginalFilename()).
                uploaderIdx(currentUser.getUID())
                .build();

        problemFileRepository.save(problemFile);
    }

    @Transactional
    public void logging(String action, String detail){
        Integer uid = currentUser.getUID();
        if (uid == null)
            throw new CustomException(ErrorCode.UNCHECKED_ERROR);

        LogStore log = LogStore.builder().memberIdx(uid).action(action).detail(detail).build();
        logStoreRepository.save(log);
    }


    @Transactional
    public void authProblemLog(Integer problemId, String authFlag, boolean isSuccess){
        Problem problem = problemRepository.findById(problemId);
        AuthLog authLog = AuthLog.builder().
                problem(problem).
                authFlag(authFlag).
                isSuccess(isSuccess).
                accountIdx(currentUser.getUID())
                .build();
        authLogRepository.save(authLog);
    }


}
