package com.penekhun.ctfjserver.Admin.Service;

import com.penekhun.ctfjserver.Config.Exception.CustomException;
import com.penekhun.ctfjserver.User.Entity.Problem;
import com.penekhun.ctfjserver.User.Repository.ProblemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static com.penekhun.ctfjserver.Config.Exception.ErrorCode.UNCHECKED_ERROR;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AdminProblemService {
    private final ProblemRepository problemRepository;

    public boolean removeProblem(Integer problemIdx){
        Problem problem = problemRepository.findById(problemIdx);
        if (problem != null) {
            problemRepository.remove(problem);
            return true;
        } else {
            throw new CustomException(UNCHECKED_ERROR);
        }
    }
}