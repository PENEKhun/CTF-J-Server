package com.penekhun.ctfjserver.User.Repository;

import com.penekhun.ctfjserver.Config.SecurityRole;
import com.penekhun.ctfjserver.User.Entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query("select a.id from Account a where a.userRole = (:role)")
    Optional<List<Long>> findAllIdByUserRole(SecurityRole role);
}
