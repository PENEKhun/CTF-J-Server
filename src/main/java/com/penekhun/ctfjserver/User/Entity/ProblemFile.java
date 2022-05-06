package com.penekhun.ctfjserver.User.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "ProblemFile")
public class ProblemFile {
    @Id
    @Column(name = "idx", nullable = false)
    private Integer id;

    @Column(name = "problem_idx")
    private Integer problemIdx;

    @Column(name = "uploader_idx", nullable = false)
    private Integer uploaderIdx;

    @Column(name = "file_name", nullable = false, length = 45)
    private String fileName;

    @Column(name = "original_file_name", nullable = false, length = 45)
    private String originalFileName;

    @Column(name = "upload_time", nullable = false)
    private Instant uploadTime;

    public Instant getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Instant uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Integer getUploaderIdx() {
        return uploaderIdx;
    }

    public void setUploaderIdx(Integer uploaderIdx) {
        this.uploaderIdx = uploaderIdx;
    }

    public Integer getProblemIdx() {
        return problemIdx;
    }

    public void setProblemIdx(Integer problemIdx) {
        this.problemIdx = problemIdx;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}