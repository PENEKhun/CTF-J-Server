package com.penekhun.ctfjserver.User.Service;

import com.penekhun.ctfjserver.User.Dto.ProblemDto;
import com.penekhun.ctfjserver.User.Entity.Account;
import com.penekhun.ctfjserver.User.Entity.Problem;
import com.penekhun.ctfjserver.User.Repository.ProblemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProblemService {


    private final ProblemRepository problemRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public ProblemDto.Default addProblem(Account account, ProblemDto.Default problemDto){
        log.info(problemDto.getTitle());
        Problem problem =  modelMapper.map(problemDto, Problem.class);
        log.info("writer is ... {}", account.getEmail());
        problem.setAuthorId(account);
        problemRepository.save(problem);
        return null;
    }

    public List<Problem> getProblemList(){
        List<Problem> problems = problemRepository.findAllProblem(false);
        return problems;
    }

    public ResponseEntity<?> authProblem(ProblemDto.Req.Auth auth){

        return null;
    }



}
