package com.penekhun.ctfjserver.User.Service;

import com.penekhun.ctfjserver.User.Dto.ProblemDto;
import com.penekhun.ctfjserver.User.Entity.Problem;
import com.penekhun.ctfjserver.User.Repository.ProblemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProblemService {

    private final ProblemRepository problemRepository;

    public ResponseEntity<String> addProblem(ProblemDto.Default problemDto){
        return null;
    }

    public ResponseEntity<String> getProblemList(){

        List<Problem> problems = problemRepository.findAllProblem(false);

        problems.forEach(a-> System.out.println(a.getFlag()));

        return null;
    }

    public ResponseEntity<?> authProblem(ProblemDto.Req.Auth auth){

        return null;
    }



}
