package com.penekhun.ctfjserver.Util;

import com.penekhun.ctfjserver.User.Entity.TokenStorage;
import com.penekhun.ctfjserver.User.Repository.TokenStorageRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RedisUtil {

    private final TokenStorageRepository refreshTokenRepository;
    private final long refreshTokenValidityInMilliseconds;
    
    public RedisUtil(TokenStorageRepository refreshTokenRepository,
                     @Value("${jwt.token-validity-in-seconds}") long tokenValidityInSeconds) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.refreshTokenValidityInMilliseconds = tokenValidityInSeconds * 1000;
    }

    public void insertTokenToStorage(TokenStorage refreshTokenEntity){
        refreshTokenEntity.setExpiration(refreshTokenValidityInMilliseconds);
        refreshTokenRepository.save(refreshTokenEntity);
    }

    public boolean TokenExist(String accessToken, String refreshToken){
        return true;
    }

}
