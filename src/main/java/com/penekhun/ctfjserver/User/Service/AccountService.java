package com.penekhun.ctfjserver.User.Service;

import com.penekhun.ctfjserver.Config.Exception.CustomException;
import com.penekhun.ctfjserver.Config.Exception.ErrorCode;
import com.penekhun.ctfjserver.Config.Jwt.JwtFilter;
import com.penekhun.ctfjserver.Config.Jwt.TokenProvider;
import com.penekhun.ctfjserver.User.Dto.AccountDto;
import com.penekhun.ctfjserver.User.Dto.TokenDto;
import com.penekhun.ctfjserver.User.Entity.Account;
import com.penekhun.ctfjserver.User.Entity.TokenStorage;
import com.penekhun.ctfjserver.User.Repository.AccountRepository;
import com.penekhun.ctfjserver.User.Repository.TokenStorageRepository;
import com.penekhun.ctfjserver.Util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final AccountRepository accountRepository;
    private final RedisUtil redisUtil;

    private final TokenStorageRepository tokenStorageRepository;

    public ResponseEntity login(String username, String password){
        //회원 검증
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Optional<Account> findMember = accountRepository.findByUsername(username);
        findMember.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        if (!encoder.matches(password, findMember.get().getPassword()))
            throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //토큰 생성및 리턴
        Map<String, String> jwt = tokenProvider.createToken(username);
        String accessToken = jwt.get("token");
        String tokenExpired = jwt.get("tokenExpired");

        String refreshToken = tokenProvider.createRefreshToken();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + accessToken);

        TokenStorage tokenStorageEntity = TokenStorage.builder()
                .refreshToken(refreshToken)
                .accessToken(accessToken)
                .username(username)
                        .build();

        redisUtil.insertTokenToStorage(username, tokenStorageEntity);
        return new ResponseEntity<>(new TokenDto(accessToken, tokenExpired, refreshToken), httpHeaders, HttpStatus.OK);
    }

    public ResponseEntity reissue(String oldAccessToken, String oldRefreshToken){
        log.info("reissue oldAccessToken: {}", oldAccessToken);
        log.info("reissue oldRefreshToken: {}", oldRefreshToken);

        Optional<TokenStorage> findAccessToken = tokenStorageRepository.findByAccessToken(oldAccessToken);
        if (findAccessToken.isEmpty())
            throw new CustomException(ErrorCode.DOESNT_EXIST_TOKEN);

        String username = findAccessToken.get().getUsername();

        log.info("reissue username : {}", username);

        if (username == null)
            throw new CustomException(ErrorCode.HANDLE_ACCESS_DENIED);

        Map<String, String> jwt = tokenProvider.createToken(username);
        String newAccessToken = jwt.get("token");
        String tokenExpired = jwt.get("tokenExpired");
        String newRefreshToken = tokenProvider.createRefreshToken();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + newAccessToken);

        TokenStorage tokenStorageEntity = TokenStorage.builder()
                .refreshToken(newRefreshToken)
                .accessToken(newAccessToken)
                .username(username)
                .build();

        redisUtil.insertTokenToStorage(username, tokenStorageEntity);
        return new ResponseEntity<>(new TokenDto(newAccessToken, tokenExpired, newRefreshToken), httpHeaders, HttpStatus.OK);
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
