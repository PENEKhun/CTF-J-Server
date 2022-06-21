package com.penekhun.ctfjserver.Util;

import com.penekhun.ctfjserver.User.Entity.TokenStorage;
import com.penekhun.ctfjserver.User.Repository.TokenStorageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class RedisUtil {

    private final TokenStorageRepository refreshTokenRepository;
    private final long refreshTokenValidityInMilliseconds;
    
    public RedisUtil(TokenStorageRepository refreshTokenRepository,
                     @Value("${jwt.refresh-token-validity-in-seconds}") long tokenValidityInSeconds) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.refreshTokenValidityInMilliseconds = tokenValidityInSeconds * 1000;
    }

    public void insertTokenToStorage(String username, TokenStorage refreshTokenEntity){
        //username으로 토큰이 존재하면 삭제하고 등록
        Optional<TokenStorage> findUser = refreshTokenRepository.findByUsername(username);
        findUser.ifPresent(tokenStorage -> refreshTokenRepository.deleteById(tokenStorage.getTokenId()));

        refreshTokenEntity.setExpiration(refreshTokenValidityInMilliseconds);
        refreshTokenRepository.save(refreshTokenEntity);
    }

}
