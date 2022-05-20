package com.penekhun.ctfjserver.Config;

import com.penekhun.ctfjserver.Config.Exception.CustomException;
import com.penekhun.ctfjserver.Config.Exception.ErrorCode;
import com.penekhun.ctfjserver.User.Entity.Account;
import com.penekhun.ctfjserver.User.Repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class CurrentUser {
    private final AccountRepository accountRepository;

    public Integer getUID(){
        String username = getUsername();
        Account account = accountRepository.findByUsername(username).orElseThrow(NullPointerException::new);
        return account.getId();
    }

    public String getUsername(){
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserDetails userDetails = (UserDetails) principal;
            return ((UserDetails) principal).getUsername();
        } catch (ClassCastException e){
            throw new CustomException(ErrorCode.HANDLE_ACCESS_DENIED);
        }
    }

    public boolean isAdmin(){
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Collection<? extends GrantedAuthority> authorities = ((UserDetails) principal).getAuthorities();
            for (GrantedAuthority auth : authorities) {
                if (auth.getAuthority().equals("ROLE_ADMIN"))
                    return true;
            }
        } catch (ClassCastException e){ //유저가 없을때 ClassCastException 일어남
            return false;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return false;
    }
}
