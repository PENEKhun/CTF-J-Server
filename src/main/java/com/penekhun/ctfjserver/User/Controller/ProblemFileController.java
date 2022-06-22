package com.penekhun.ctfjserver.User.Controller;

import com.penekhun.ctfjserver.User.Service.UploadFileService;
import io.swagger.v3.oas.annotations.Operation;
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
    private final UploadFileService uploadFileService;

    @GetMapping("{fileName}")
    @Operation(security = { @SecurityRequirement(name = "bearer-key")},
            tags= {"problem", "admin.problem"}, summary = "파일 다운로드 API", description = "file download API")
    public ResponseEntity<byte[]> download(@PathVariable @Valid @NotEmpty String fileName){
        return uploadFileService.downloadFile(fileName);
    }
}
