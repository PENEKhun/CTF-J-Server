package com.penekhun.ctfjserver.User.Service;

import com.penekhun.ctfjserver.Config.Exception.CustomException;
import com.penekhun.ctfjserver.Config.Exception.ErrorCode;
import com.penekhun.ctfjserver.Config.Jwt.JwtFilter;
import com.penekhun.ctfjserver.Config.Jwt.TokenProvider;
import com.penekhun.ctfjserver.User.Dto.AccountDto;
import com.penekhun.ctfjserver.User.Dto.TokenDto;
import com.penekhun.ctfjserver.User.Entity.Account;
import com.penekhun.ctfjserver.User.Repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final AccountRepository accountRepository;


    public ResponseEntity login(String username, String password){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Optional<Account> findMember = accountRepository.findByUsername(username);
        findMember.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        if (!encoder.matches(password, findMember.get().getPassword()))
            throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.createToken(authentication);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        return new ResponseEntity<>(new TokenDto(jwt), httpHeaders, HttpStatus.OK);
    }

    public ResponseEntity signup(AccountDto.Req.Signup signup){
        Optional<Account> findMember = accountRepository.findByUsername(signup.getUsername());
        findMember.ifPresent(then -> {
            throw new CustomException(ErrorCode.USERNAME_DUPLICATION);
        });

        findMember = accountRepository.findOneByEmail(signup.getEmail());
        findMember.ifPresent(then -> {
            throw new CustomException(ErrorCode.EMAIL_DUPLICATION);
        });

        findMember = accountRepository.findOneByNickname(signup.getNickname());
        findMember.ifPresent(then -> {
            throw new CustomException(ErrorCode.NICKNAME_DUPLICATION);
        });

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        Account account = new Account();
        account.setUsername(signup.getUsername());
        account.setPassword(
                bCryptPasswordEncoder.encode(signup.getPassword()));
        account.setEmail(signup.getEmail());
        account.setNickname(signup.getNickname());
        account.setRealName(signup.getRealName());

        accountRepository.save(account);





        return null;
    }
}
