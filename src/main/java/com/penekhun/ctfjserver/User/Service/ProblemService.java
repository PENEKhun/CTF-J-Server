package com.penekhun.ctfjserver.User.Service;

import com.penekhun.ctfjserver.Config.Exception.CustomException;
import com.penekhun.ctfjserver.Config.Exception.ErrorCode;
import com.penekhun.ctfjserver.User.Dto.ProblemDto;
import com.penekhun.ctfjserver.User.Entity.Account;
import com.penekhun.ctfjserver.User.Entity.Problem;
import com.penekhun.ctfjserver.User.Repository.AccountRepository;
import com.penekhun.ctfjserver.User.Repository.ProblemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProblemService {

    private final ProblemRepository problemRepository;
    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;
    private final LogService logService;

    @Transactional
    public ProblemDto.Default addProblem(Account account, ProblemDto.Default problemDto){
        log.info(problemDto.getTitle());
        Problem problem =  modelMapper.map(problemDto, Problem.class);
        problem.setAuthorId(account);
        problemRepository.save(problem);
        logService.uploadProblemLog(problem);
        return null;
    }

    public List<Problem> getProblemList(){
        List<Problem> problems = problemRepository.findAllProblem(false);
        return problems;
    }

    public List<Problem> getProblemListFromCategory(String category){
        List<Problem> problems = problemRepository.findByCategory(category);
        return problems;
    }

    @Transactional
    public boolean authProblem(Account account, Integer problemId, ProblemDto.Req.Auth auth){

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
