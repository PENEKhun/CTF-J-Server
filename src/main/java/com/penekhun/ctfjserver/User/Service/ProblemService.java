package com.penekhun.ctfjserver.User.Service;

import com.penekhun.ctfjserver.Config.Exception.CustomException;
import com.penekhun.ctfjserver.Config.Exception.ErrorCode;
import com.penekhun.ctfjserver.User.Dto.ProblemDto;
import com.penekhun.ctfjserver.User.Dto.RankDto;
import com.penekhun.ctfjserver.User.Entity.Account;
import com.penekhun.ctfjserver.User.Entity.Problem;
import com.penekhun.ctfjserver.User.Repository.AccountRepository;
import com.penekhun.ctfjserver.User.Repository.ProblemRepository;
import com.penekhun.ctfjserver.Util.RankSchedule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProblemService {

    private final ProblemRepository problemRepository;
    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;
    private final LogService logService;
    private final RankSchedule rankSchedule;

    @Transactional
    public ProblemDto.Default addProblem(Account account, ProblemDto.Default problemDto){
        Problem problem =  modelMapper.map(problemDto, Problem.class);
        problem.setAuthorId(account);
        problemRepository.save(problem);
        logService.uploadProblemLog(problem);
        return problemDto;
    }

    public List<RankDto.ProbWithDynamicScore> getProblemList(boolean includePrivate){
        List<RankDto.ProbWithDynamicScore> probSolveCntList = rankSchedule.getPrbSolveList();
        return includePrivate ? probSolveCntList : probSolveCntList.stream()
                .filter(RankDto.ProbWithDynamicScore::isPublic)
                .collect(Collectors.toList());
    }

    public List<RankDto.ProbWithDynamicScore> getProblemListFromCategory(String category, boolean includePrivate){
        List<RankDto.ProbWithDynamicScore> probSolveCntList = rankSchedule.getPrbSolveList();

        return includePrivate ? probSolveCntList.stream()
                .filter(prob -> prob.getType().equalsIgnoreCase(category))
                .collect(Collectors.toList()) : probSolveCntList.stream()
                .filter(prob -> prob.getType().equalsIgnoreCase(category) && prob.isPublic())
                .collect(Collectors.toList());
    }

    @Transactional
    public boolean authProblem(Account account, Long problemId, ProblemDto.Req.Auth auth){

        if (account.isAdmin())
            throw new CustomException(ErrorCode.ONLY_ACCESS_USER);

        if (problemRepository.amICorrectBefore(account.getId(), problemId))
            throw new CustomException(ErrorCode.ALREADY_CORRECT);

        Problem problem = problemRepository.findById(problemId);
        if (problem == null)
            return false;
        if (Boolean.FALSE.equals(problem.isPublic()))
            return false;
        if (!problem.isCorrect(auth.getFlag()))
            return false;

        /*
            Logging은 Controller에서 수행합니다.
         */

        account.updateLastAuthTime();
        accountRepository.save(account);

        return true;
    }
}
