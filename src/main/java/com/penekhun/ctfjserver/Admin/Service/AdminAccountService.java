package com.penekhun.ctfjserver.Admin.Service;

import com.penekhun.ctfjserver.Config.Exception.CustomException;
import com.penekhun.ctfjserver.Config.Exception.ErrorCode;
import com.penekhun.ctfjserver.User.Entity.Account;
import com.penekhun.ctfjserver.User.Repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Secured("ROLE_ADMIN")
@RequiredArgsConstructor
@Transactional
public class AdminAccountService {
    private final AccountRepository accountRepository;

    public void removeAccount(final Long id){
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));


        accountRepository.delete(account);
    }
}
