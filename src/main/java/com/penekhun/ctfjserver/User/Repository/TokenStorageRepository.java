package com.penekhun.ctfjserver.User.Repository;

import com.penekhun.ctfjserver.User.Entity.TokenStorage;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TokenStorageRepository extends CrudRepository<TokenStorage, Long> {
    Optional<TokenStorage> findFirstByRefreshTokenAndKeyUsername(String refreshToken, String username);
    Optional<TokenStorage> findFirstByRefreshToken(String refreshToken);
    Optional<TokenStorage> findByAccessToken(String accessToken);
}
