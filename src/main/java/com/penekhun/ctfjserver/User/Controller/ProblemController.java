package com.penekhun.ctfjserver.User.Controller;

import com.penekhun.ctfjserver.User.Dto.ProblemDto;
import com.penekhun.ctfjserver.User.Service.ProblemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/problem")
@Slf4j
@RequiredArgsConstructor
public class ProblemController {

    private final ProblemService problemService;

    @GetMapping("")
    public ResponseEntity<String> getProblemListMapping(){
        return problemService.getProblemList();
    }

    @PutMapping("")
    public ResponseEntity<?> addProblemMapping(@Valid ProblemDto.Default problemDto){
        return problemService.addProblem(problemDto);
    }

    @DeleteMapping("{problem}")
    public ResponseEntity<?> deleteProblemMapping(){
        return null;
    }


    @PostMapping("{problem}")
    public ResponseEntity<?> authProblemMapping(@Valid ProblemDto.Req.Auth auth){
        return problemService.authProblem(auth);
    }

}
