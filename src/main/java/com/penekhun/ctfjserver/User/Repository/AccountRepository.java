package com.penekhun.ctfjserver.User.Repository;

import com.penekhun.ctfjserver.User.Entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    public Optional<Account> findByUsername(String username);
    public Optional<Account> findOneByEmail(String email);
    public Optional<Account> findOneByNickname(String nickname);

}
