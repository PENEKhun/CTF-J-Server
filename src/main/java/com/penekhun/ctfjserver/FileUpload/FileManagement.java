package com.penekhun.ctfjserver.FileUpload;

import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.io.InputStream;

public interface FileManagement {
    void uploadFile(InputStream inputStream, ObjectMetadata objectMetadata, String fileName);
    void deleteFile(String key);
    String getFileUrl(String fileName);
    ResponseEntity<byte[]> downloadFile(String storedFileName) throws IOException;
}
