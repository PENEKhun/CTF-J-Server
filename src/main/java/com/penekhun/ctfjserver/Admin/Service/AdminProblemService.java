package com.penekhun.ctfjserver.Admin.Service;

import com.penekhun.ctfjserver.Config.Exception.CustomException;
import com.penekhun.ctfjserver.Config.Exception.ErrorCode;
import com.penekhun.ctfjserver.User.Dto.ProblemDto;
import com.penekhun.ctfjserver.User.Entity.Problem;
import com.penekhun.ctfjserver.User.Repository.ProblemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static com.penekhun.ctfjserver.Config.Exception.ErrorCode.UNCHECKED_ERROR;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
@Secured("ROLE_ADMIN")
public class AdminProblemService {
    private final ProblemRepository problemRepository;

    public Problem editProblemPartly(final Long id, final ProblemDto.DefaultNoValid editInfo) throws DataIntegrityViolationException {
        Problem problem = problemRepository.findById(id);
        if (problem == null)
            throw new CustomException(ErrorCode.HANDLE_ACCESS_DENIED);

        problem.partlyEdit(editInfo);
        return problemRepository.save(problem);
    }

    public boolean removeProblem(Long problemIdx){
        Problem problem = problemRepository.findById(problemIdx);
        if (problem != null) {
            problemRepository.remove(problem);
            return true;
        } else {
            throw new CustomException(UNCHECKED_ERROR);
        }
    }
}
