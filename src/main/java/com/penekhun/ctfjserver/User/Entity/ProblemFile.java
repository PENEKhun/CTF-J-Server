package com.penekhun.ctfjserver.User.Entity;

import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "ProblemFile", schema = "ctf")
@NoArgsConstructor
public class ProblemFile {

    @Builder
    public ProblemFile(Long uploaderIdx, String fileName, String originalFileName) {
        this.uploaderIdx = uploaderIdx;
        this.fileName = fileName;
        this.originalFileName = originalFileName;
        this.problemIdx = null;
    }

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "idx", nullable = false)
    private Integer id;

    @Column(name = "problem_idx")
    private Long problemIdx;

    @Column(name = "uploader_idx", nullable = false)
    private Long uploaderIdx;

    @Column(name = "file_name", nullable = false, length = 45)
    private String fileName;

    @Column(name = "original_file_name", nullable = false, length = 45)
    private String originalFileName;

    @CreatedDate
    @Column(name = "upload_time", nullable = false, updatable = false)
    private Timestamp uploadTime;

    public Integer getId() {
        return id;
    }
    public void setProblemIdx(Long problemIdx) {
        this.problemIdx = problemIdx;
    }


}