package com.penekhun.ctfjserver.User.Controller;

import com.penekhun.ctfjserver.Config.CurrentUserParameter;
import com.penekhun.ctfjserver.Config.Exception.CustomException;
import com.penekhun.ctfjserver.Config.Exception.ErrorCode;
import com.penekhun.ctfjserver.User.Dto.ProblemDto;
import com.penekhun.ctfjserver.User.Dto.RankDto;
import com.penekhun.ctfjserver.User.Entity.Account;
import com.penekhun.ctfjserver.User.Service.LogService;
import com.penekhun.ctfjserver.User.Service.ProblemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @GetMapping("")
    @Operation(security = { @SecurityRequirement(name = "bearer-key")},
            tags= {"problem"}, summary = "공개된 문제 전체를 가져오는 API", description = "get ALL public Problem API")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<RankDto.ProbWithDynamicScore>> getProblemListMapping(){
        return new ResponseEntity<>(problemService.getProblemList(false), HttpStatus.OK);
    }
    
    @PostMapping("{problemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(security = { @SecurityRequirement(name = "bearer-key")},
            tags= {"problem"}, summary = "문제 플래그 인증 API", description = "auth FLAG API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(examples = @ExampleObject(value=""))),
            @ApiResponse(responseCode = "501", description = "어드민은 문제를 풀 수 없음", ref = "#/components/responses/ErrorCode.ONLY_ACCESS_USER"),
            @ApiResponse(responseCode = "403", description = "이미 맞춤", ref = "#/components/responses/ErrorCode.ALREADY_CORRECT"),
            @ApiResponse(responseCode = "404", description = "잘못된 플래그(오답)", ref = "#/components/responses/ErrorCode.INCORRECT_FLAG")})
    public ResponseEntity<String> authProblemMapping(@CurrentUserParameter Account account, @PathVariable @Validated @NotNull Long problemId, @Valid ProblemDto.Req.Auth auth){
        boolean isCorrect = problemService.authProblem(account, problemId, auth);
        logService.authProblemLog(problemId, auth.getFlag(), isCorrect);
        if (isCorrect)
            return null;
        else throw new CustomException(ErrorCode.INCORRECT_FLAG);
    }

    @GetMapping("{category}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(security = { @SecurityRequirement(name = "bearer-key")},
            tags= {"problem"}, summary = "카테고리를 통해 문제 리스트를 가져오는 API", description = "get problem from category API")
    public List<RankDto.ProbWithDynamicScore> getProblemFromCategoryMapping(
            @Parameter(name = "category", description = "문제 종류", schema = @Schema(type = "string", allowableValues = {"pwnable", "web", "reversing", "crypto", "misc"}))
            @PathVariable @Validated @NotNull String category){
        return problemService.getProblemListFromCategory(category, false);

    }

}
