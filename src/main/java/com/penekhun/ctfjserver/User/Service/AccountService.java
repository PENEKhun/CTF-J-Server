package com.penekhun.ctfjserver.User.Service;

import com.penekhun.ctfjserver.Config.Jwt.JwtFilter;
import com.penekhun.ctfjserver.Config.Jwt.TokenProvider;
import com.penekhun.ctfjserver.User.Dto.AccountDto;
import com.penekhun.ctfjserver.User.Dto.TokenDto;
import com.penekhun.ctfjserver.User.Entity.Account;
import com.penekhun.ctfjserver.User.Repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.NameAlreadyBoundException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final AccountRepository accountRepository;


    public ResponseEntity login(String username, String password){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Optional<Account> findMember = accountRepository.findOneByUsername(username);
        findMember.orElseThrow(() -> new NullPointerException("회원정보가 없습니다."));
        if (!encoder.matches(password, findMember.get().getPassword()))
            throw new NullPointerException("회원정보가 없습니다.");

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
        Optional<Account> findMember = accountRepository.findOneByUsername(signup.getUsername());
        findMember.ifPresent(then -> {
            throw new RuntimeException("존재하는 아이디입니다.");
        });

        findMember = accountRepository.findOneByEmail(signup.getEmail());
        findMember.ifPresent(then -> {
            throw new RuntimeException("존재하는 이메일입니다.");
        });

        findMember = accountRepository.findOneByNickname(signup.getNickname());
        findMember.ifPresent(then -> {
            throw new RuntimeException("존재하는 닉네임입니다.");
        });

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        Account account = new Account();
        account.setUsername(signup.getUsername());
        account.setPassword(
                bCryptPasswordEncoder.encode(signup.getPassword()));
        account.setEmail(signup.getEmail());
        account.setNickname(signup.getNickname());
        account.setRealName(signup.getRealName());
        account.setUserRole("ROLE_USER");

        accountRepository.save(account);





        return null;
    }
}
