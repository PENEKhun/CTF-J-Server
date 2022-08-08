package com.penekhun.ctfjserver.Admin.Service;

import com.penekhun.ctfjserver.Config.Exception.CustomException;
import com.penekhun.ctfjserver.Config.Exception.ErrorCode;
import com.penekhun.ctfjserver.User.Dto.ProblemDto;
import com.penekhun.ctfjserver.User.Entity.Problem;
import com.penekhun.ctfjserver.User.Repository.ProblemFileRepository;
import com.penekhun.ctfjserver.User.Repository.ProblemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
@Secured("ROLE_ADMIN")
public class AdminProblemService {
    private final ProblemRepository problemRepository;
    private final ProblemFileRepository problemFileRepository;

    public Problem editProblemPartly(final Long id, final ProblemDto.DefaultNoValid editInfo) throws DataIntegrityViolationException {
        Problem problem = problemRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.HANDLE_ACCESS_DENIED));

        // 문제 정보 변경
        problem.partlyEdit(editInfo);
        if (editInfo.getFileIdx() != null){

            // 기존 파일 관계 제거
            problemFileRepository.findByProblemIdx(id)
                    .ifPresent(problemFile -> {
                        problemFile.setProblemIdx(null);
                        problemFileRepository.save(problemFile);
                    });

            // 새로운 파일 관계 형성
            problemFileRepository.findById(editInfo.getFileIdx())
                    .ifPresent(problemFile -> {
                        problemFile.setProblemIdx(problem.getId());
                        problemFileRepository.save(problemFile);
                    });
        }
        return problemRepository.save(problem);
    }

    public void removeProblem(Long problemIdx){
//        Problem problem = problemRepository.findById(problemIdx)
//                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_INPUT_VALUE));
//        if (problem != null) {
            problemRepository.deleteById(problemIdx);
//            return true;
//        } else {
//            throw new CustomException(UNCHECKED_ERROR);
//        }
    }
}
