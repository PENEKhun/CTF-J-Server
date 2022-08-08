package com.penekhun.ctfjserver.Admin.Service;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.penekhun.ctfjserver.Config.Exception.CustomException;
import com.penekhun.ctfjserver.Config.Exception.ErrorCode;
import com.penekhun.ctfjserver.FileUpload.FileManagement;
import com.penekhun.ctfjserver.User.Dto.ProblemDto;
import com.penekhun.ctfjserver.User.Entity.Account;
import com.penekhun.ctfjserver.User.Entity.ProblemFile;
import com.penekhun.ctfjserver.User.Repository.ProblemFileRepository;
import com.penekhun.ctfjserver.User.Service.LogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
@Secured("ROLE_ADMIN")
public class AdminFileService {

    private final FileManagement s3Service;
    private final LogService logService;
    private final ProblemFileRepository problemFileRepository;

    public ProblemDto.Res.File uploadFile(MultipartFile file, Account uploader) {
        String fileName = createFileName(file.getOriginalFilename());
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());
        log.info("attempt file upload : {}", fileName);
        try (InputStream inputStream = file.getInputStream()) {
            s3Service.uploadFile(inputStream, objectMetadata, fileName);
        } catch (IOException e) {
            throw new CustomException(ErrorCode.FILE_UPLOAD_FAIL);
        }

        // 업로드 성공시 파일 정보 저장
        logService.logging("uploadProblemFile", String.format("%s", file.getOriginalFilename()));

        ProblemFile problemFile = ProblemFile.builder()
                .fileName(fileName)
                .fileNameForDisplay(displayName)
                .originalFileName(file.getOriginalFilename())
                .uploaderIdx(uploader.getId())
                .build();

        problemFileRepository.save(problemFile);
        return new ProblemDto.Res.File(problemFile.getId(), fileName);
    }

    public ResponseEntity<byte[]> downloadFile(@NotEmpty String fileName){
        try {
            return s3Service.downloadFile(fileName);
        } catch (IOException e) {
            e.printStackTrace();
            throw new CustomException(ErrorCode.UNCHECKED_ERROR);
        }
    }

    public void deleteFile(@NotEmpty String fileName){
        s3Service.deleteFile(fileName);
    }

    // 기존 확장자명을 유지한 채, 유니크한 파일의 이름을 생성하는 로직
    private String createFileName(String originalFileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(originalFileName));
    }

    // 파일의 확장자명을 가져오는 로직
    private String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new IllegalArgumentException(String.format("잘못된 형식의 파일 (%s) 입니다", fileName));
        }
    }
}

