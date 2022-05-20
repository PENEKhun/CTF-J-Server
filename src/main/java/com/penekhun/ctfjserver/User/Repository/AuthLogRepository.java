package com.penekhun.ctfjserver.User.Repository;

import com.penekhun.ctfjserver.User.Entity.AuthLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthLogRepository extends JpaRepository<AuthLog, Long> {
}
