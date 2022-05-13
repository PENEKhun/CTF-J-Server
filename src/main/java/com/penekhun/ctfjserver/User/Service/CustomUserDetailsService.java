package com.penekhun.ctfjserver.User.Service;

import com.penekhun.ctfjserver.Config.SecurityUser;
import com.penekhun.ctfjserver.User.Entity.Account;
import com.penekhun.ctfjserver.User.Repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final AccountRepository accountRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("called CustomUserDetails.Service -> loadUserByUsername : {} ", username);
        Optional<Account> findMember = accountRepository.findByUsername(username);

        if (findMember.isEmpty()) throw new UsernameNotFoundException("username not found : " + username);

        log.info("loadUserByUsername account.username = {}", username);
        return new SecurityUser(findMember.get());
    }
}
