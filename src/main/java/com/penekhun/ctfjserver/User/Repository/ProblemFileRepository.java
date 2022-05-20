package com.penekhun.ctfjserver.User.Repository;

import com.penekhun.ctfjserver.User.Entity.ProblemFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProblemFileRepository extends JpaRepository<ProblemFile, Integer> {
}
