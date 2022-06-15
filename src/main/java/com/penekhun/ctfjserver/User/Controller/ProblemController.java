package com.penekhun.ctfjserver.User.Controller;

import com.penekhun.ctfjserver.Config.CurrentUserParameter;
import com.penekhun.ctfjserver.Config.Exception.CustomException;
import com.penekhun.ctfjserver.Config.Exception.ErrorCode;
import com.penekhun.ctfjserver.User.Dto.ProblemDto;
import com.penekhun.ctfjserver.User.Entity.Account;
import com.penekhun.ctfjserver.User.Service.LogService;
import com.penekhun.ctfjserver.User.Service.ProblemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/api/v1/problem")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "problem", description = "문제 관련 API 모음")
public class ProblemController {

    private final ProblemService problemService;
    private final LogService logService;
    private final ModelMapper modelMapper;

    @GetMapping("")
    @Operation(tags= {"problem"}, summary = "공개된 문제 전체를 가져오는 API", description = "get ALL public Problem API")
    public ResponseEntity<List<RankDto.ProbWithDynamicScore>> getProblemListMapping(){
        return new ResponseEntity<>(problemService.getProblemList(), HttpStatus.OK);
    }

    //@Secured("ROLE_ADMIN")
    @PostMapping("")
    @Operation(tags= {"problem"}, summary = "문제 등록하는 API", description = "make Problem API")
    public ResponseEntity<ProblemDto.Default> addProblemMapping(@CurrentUserParameter Account account, ProblemDto.Default problemDto){
        if (account == null)
            throw new CustomException(ErrorCode.HANDLE_ACCESS_DENIED);

        return new ResponseEntity<>(problemService.addProblem(account, problemDto), HttpStatus.CREATED);
    }

    @Secured("ROLE_ADMIN")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{problem}")
    //todo : 문제 삭제 구현
    public ResponseEntity<?> deleteProblemMapping(){
        return null;
    }


    @PostMapping("{problemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(tags= {"problem"}, summary = "문제 플래그 인증 API", description = "auth FLAG API")
    public ResponseEntity<String> authProblemMapping(@CurrentUserParameter Account account, @PathVariable @Validated @NotNull Integer problemId, @Valid ProblemDto.Req.Auth auth){
        boolean isCorrect = problemService.authProblem(account, problemId, auth);
        logService.authProblemLog(problemId, auth.getFlag(), isCorrect);
        if (isCorrect)
            return null;
        else throw new CustomException(ErrorCode.INCORRECT_FLAG);
    }

    @GetMapping("{category}")
    @Operation(tags= {"problem"}, summary = "카테고리를 통해 문제 리스트를 가져오는 API", description = "get problem from category API")
    public List<RankDto.ProbWithDynamicScore> getProblemFromCategoryMapping(@PathVariable @Validated @NotNull String category){
        return problemService.getProblemListFromCategory(category);

    }

}
