package com.penekhun.ctfjserver.Config;

import com.penekhun.ctfjserver.User.Entity.Account;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

@Slf4j
@Getter
public class SecurityUser extends User {

    private Account account;

    public SecurityUser(Account account) {
        super(account.getUsername(), account.getPassword(), AuthorityUtils.createAuthorityList(account.getRole().toString()));

        log.info("SecurityUser account.uid = {}", account.getId());
        log.info("SecurityUser account.username = {}", account.getUsername());
        log.info("SecurityUser account.password = {}", account.getPassword());
        log.info("SecurityUser account.role = {}", account.getRole());

        this.account = account;
    }
}
