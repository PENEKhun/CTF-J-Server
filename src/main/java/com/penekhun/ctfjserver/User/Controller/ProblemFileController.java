package com.penekhun.ctfjserver.User.Controller;

import com.penekhun.ctfjserver.User.Service.UploadFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RequestMapping("/api/v1/file")
@Controller
@Slf4j
@RequiredArgsConstructor
public class ProblemFileController {
    private final UploadFileService uploadFileService;

    @PostMapping("")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<String> upload(@Valid @NotNull MultipartFile file){
        return ResponseEntity.ok().body(uploadFileService.uploadFile(file));
    }

    @GetMapping("{fileName}")
    public ResponseEntity<byte[]> download(@PathVariable @Valid @NotEmpty String fileName){
        return uploadFileService.downloadFile(fileName);

    }
}
