package com.penekhun.ctfjserver.User.Controller;

import com.penekhun.ctfjserver.Config.CurrentUserParameter;
import com.penekhun.ctfjserver.Config.Exception.CustomException;
import com.penekhun.ctfjserver.Config.Exception.ErrorCode;
import com.penekhun.ctfjserver.User.Dto.ProblemDto;
import com.penekhun.ctfjserver.User.Entity.Account;
import com.penekhun.ctfjserver.User.Entity.Problem;
import com.penekhun.ctfjserver.User.Service.LogService;
import com.penekhun.ctfjserver.User.Service.ProblemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/problem")
@Slf4j
@RequiredArgsConstructor
public class ProblemController {

    private final ProblemService problemService;
    private final LogService logService;
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
    public ProblemDto.Default addProblemMapping(@CurrentUserParameter Account account, @Valid ProblemDto.Default problemDto){
        if (account == null)
            throw new CustomException(ErrorCode.HANDLE_ACCESS_DENIED);

        return problemService.addProblem(account, problemDto);
    }

    @DeleteMapping("{problem}")
    public ResponseEntity<?> deleteProblemMapping(){
        return null;
    }


    @PostMapping("{problemId}")
    public ResponseEntity<String> authProblemMapping(@CurrentUserParameter Account account, @PathVariable @Validated @NotNull Integer problemId, @Valid ProblemDto.Req.Auth auth){
        boolean isCorrect = problemService.authProblem(account, problemId, auth);
        logService.authProblemLog(problemId, auth.getFlag(), isCorrect);
        if (isCorrect)
            return ResponseEntity.ok().body("true");
        else return ResponseEntity.ok().body("false");
    }

}
