package com.penekhun.ctfjserver.User.Controller;

import com.penekhun.ctfjserver.User.Dto.ProblemDto;
import com.penekhun.ctfjserver.User.Entity.Problem;
import com.penekhun.ctfjserver.User.Service.ProblemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/problem")
@Slf4j
@RequiredArgsConstructor
public class ProblemController {

    private final ProblemService problemService;
    private final ModelMapper modelMapper;

    @GetMapping("")
    public List<ProblemDto.Res.problemWithoutFlag> getProblemListMapping(){
        List<Problem> problems = problemService.getProblemList();
        List<ProblemDto.Res.problemWithoutFlag> problemsNoFlag = new ArrayList<>();
        problems.forEach(problem -> problemsNoFlag.add(modelMapper.map(problem, ProblemDto.Res.problemWithoutFlag.class)));

        return problemsNoFlag;
    }

    //@Secured("ROLE_ADMIN")
    @PostMapping("")
    public ProblemDto.Default addProblemMapping( @Valid ProblemDto.Default problemDto){
//        @CurrentUser Account account,
//        if (account == null)
//            throw new CustomException(ErrorCode.HANDLE_ACCESS_DENIED);

       // return problemService.addProblem(account, problemDto);
        return null;
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
