package com.penekhun.ctfjserver.FileUpload;

import com.amazonaws.services.s3.model.ObjectMetadata;

import java.io.InputStream;

public interface UploadFactory {
    void uploadFile(InputStream inputStream, ObjectMetadata objectMetadata, String fileName);
    String getFileUrl(String fileName);
}
