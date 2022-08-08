package com.penekhun.ctfjserver.User.Repository;

import com.penekhun.ctfjserver.User.Entity.ProblemFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProblemFileRepository extends JpaRepository<ProblemFile, Long> {
    Optional<ProblemFile> findByProblemIdx(Long problemIdx);

}
