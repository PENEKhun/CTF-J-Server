package com.penekhun.ctfjserver.Config;

import com.penekhun.ctfjserver.User.Entity.Account;
import com.penekhun.ctfjserver.User.Repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrentUser {
    private final AccountRepository accountRepository;

    public Integer getUID(){
        String username = getUsername();
        Account account = accountRepository.findByUsername(username).orElseThrow(NullPointerException::new);
        return account.getId();
    }

    public String getUsername(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails)principal;
        return ((UserDetails) principal).getUsername();
    }

}
