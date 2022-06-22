package com.penekhun.ctfjserver.Admin.Controller;

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
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/problem")
@Slf4j
@RequiredArgsConstructor
@Secured("ROLE_ADMIN")
@Tag(name = "admin.problem", description = "[관리자 권한] 문제 관련 API 모음")
public class AdminProblemController {

    private final ProblemService problemService;
    private final LogService logService;
    private final ModelMapper modelMapper;

    @GetMapping("")
    @Operation(security = { @SecurityRequirement(name = "bearer-key")},
            tags= {"admin.problem"}, summary = "문제 전체(비공개 문제 포함)를 가져오는 API", description = "get ALL Problem API")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<RankDto.ProbWithDynamicScore>> getProblemListMapping(){
        return new ResponseEntity<>(problemService.getProblemList(true), HttpStatus.OK);
    }

    @PostMapping("")
    @Operation(security = { @SecurityRequirement(name = "bearer-key")},
            tags= {"admin.problem"}, summary = "문제 등록하는 API", description = "make Problem API")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "성공", content = @Content(schema = @Schema(implementation = ProblemDto.Default.class))),
            @ApiResponse(responseCode = "403", description = "잘못된 접근", ref = "#/components/responses/ErrorCode.HANDLE_ACCESS_DENIED")})
    public ResponseEntity<ProblemDto.Default> addProblemMapping(@CurrentUserParameter Account account, ProblemDto.Default problemDto){
        if (account == null)
            throw new CustomException(ErrorCode.HANDLE_ACCESS_DENIED);

        return new ResponseEntity<>(problemService.addProblem(account, problemDto), HttpStatus.CREATED);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{problem}")
    @Operation(security = { @SecurityRequirement(name = "bearer-key")},
            tags= {"admin.problem"}, summary = "문제 삭제하는 API - 미완성", description = "delete Problem API")
    //todo : 문제 삭제 구현
    public ResponseEntity<?> deleteProblemMapping(){
        return null;
    }

    @GetMapping("{category}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(security = { @SecurityRequirement(name = "bearer-key")},
            tags= {"admin.problem"}, summary = "카테고리를 통해 문제 리스트를(비공개 문제 포함) 가져오는 API", description = "get problem from category API")
    public List<RankDto.ProbWithDynamicScore> getProblemFromCategoryMapping(
            @Parameter(name = "category", description = "문제 종류", schema = @Schema(type = "string", allowableValues = {"pwnable", "web", "reversing", "crypto", "misc"}))
            @PathVariable @Validated @NotNull String category){
        return problemService.getProblemListFromCategory(category, true);

    }

}
