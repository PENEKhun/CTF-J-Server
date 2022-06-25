package com.penekhun.ctfjserver.Admin.Controller;

import com.penekhun.ctfjserver.Admin.Service.AdminFileService;
import com.penekhun.ctfjserver.Config.Exception.CustomException;
import com.penekhun.ctfjserver.Config.Exception.ErrorCode;
import com.penekhun.ctfjserver.User.Dto.ProblemDto;
import com.penekhun.ctfjserver.User.Service.UploadFileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RequestMapping("/api/v1/admin/file")
@Controller
@Slf4j
@RequiredArgsConstructor
@Secured("ROLE_ADMIN")
public class AdminProblemFileController {
    private final AdminFileService fileService;
    private final LogService logService;

    @PostMapping("")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ProblemDto.Res.File.class))),
            @ApiResponse(responseCode = "500", description = "업로드 실패", ref = "#/components/responses/ErrorCode.FILE_UPLOAD_FAIL")})
    @Operation(security = { @SecurityRequirement(name = "bearer-key")},
            tags= {"admin.problem"}, summary = "파일 업로드 API", description = "upload file and response file url. only for admin")
    public ResponseEntity<ProblemDto.Res.File> upload(@Valid @NotNull MultipartFile file){
        String fileUrl = fileService.uploadFile(file);
        if (fileUrl != null || file.isEmpty()) {
            ProblemDto.Res.File responseFile = new ProblemDto.Res.File();
            responseFile.setUrl(fileUrl);
            return new ResponseEntity<>(responseFile, HttpStatus.OK);
        } else {
            throw new CustomException(ErrorCode.FILE_UPLOAD_FAIL);
        }
    }

    @DeleteMapping("{fileUUID}")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "성공"),
            @ApiResponse(responseCode = "500", description = "삭제 실패", ref = "#/components/responses/ErrorCode.FILEqwdqdqwq3wdcasc_UPLOAD_FAIL")})
    @Operation(security = { @SecurityRequirement(name = "bearer-key")},
            tags= {"admin.problem"}, summary = "파일 삭제 API", description = "remove file")
    public ResponseEntity removeFile(@PathVariable String fileUUID){
        fileService.deleteFile(fileUUID);
        logService.logging("remove file", String.format("file detail : %s", fileUUID));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "성공"),
            @ApiResponse(responseCode = "500", description = "??", ref = "#/components/responses/ErrorCode.FILEqwdqdqwq3wdcasc_UPLOAD_FAIL")})
    @Operation(security = { @SecurityRequirement(name = "bearer-key")},
            tags= {"admin.problem"}, summary = "파일 리스트 API - 미구현", description = "listing file")
    public void getFileList(){

    }

}
