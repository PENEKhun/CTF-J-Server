package com.penekhun.ctfjserver.User.Controller;

import com.penekhun.ctfjserver.Admin.Service.AdminFileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@RequestMapping("/api/v1/file")
@Controller
@Slf4j
@RequiredArgsConstructor
public class ProblemFileController {
    private final AdminFileService uploadFileService;

    @GetMapping("{fileName}")
    @Operation(security = { @SecurityRequirement(name = "bearer-key")},
            tags= {"problem", "admin.problem"}, summary = "파일 다운로드 API", description = "file download API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
            @ApiResponse(responseCode = "500", description = "알수없는 오류", ref= "#/components/responses/ErrorCode.UNCHECKED_ERROR")})
    public ResponseEntity<byte[]> download(@PathVariable @Valid @NotEmpty String fileName){
        return uploadFileService.downloadFile(fileName);
    }
}
