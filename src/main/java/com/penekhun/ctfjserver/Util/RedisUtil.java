package com.penekhun.ctfjserver.Util;

import com.penekhun.ctfjserver.User.Entity.TokenStorage;
import com.penekhun.ctfjserver.User.Repository.TokenStorageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisUtil {

    private final TokenStorageRepository refreshTokenRepository;

    public void insertTokenToStorage(TokenStorage refreshTokenEntity){
        refreshTokenRepository.save(refreshTokenEntity);
    }

    public boolean TokenExist(String accessToken, String refreshToken){
        return true;
    }

}
