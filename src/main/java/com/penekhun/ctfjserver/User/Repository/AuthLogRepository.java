package com.penekhun.ctfjserver.User.Repository;

import com.penekhun.ctfjserver.User.Entity.AuthLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

public interface AuthLogRepository extends JpaRepository<AuthLog, Long> {
    @Query("select (count(a) > 0) from AuthLog a where a.accountIdx = ?1 and a.problem.id = ?2 and a.isSuccess = true")
    boolean amICorrectBefore(@NonNull Long accountIdx, @NonNull Long problemId);
}
