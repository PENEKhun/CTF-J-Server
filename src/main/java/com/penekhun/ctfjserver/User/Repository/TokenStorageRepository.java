package com.penekhun.ctfjserver.User.Repository;

import com.penekhun.ctfjserver.User.Entity.TokenStorage;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TokenStorageRepository extends CrudRepository<TokenStorage, Long> {
    Optional<TokenStorage> findByAccessToken(String accessToken);
    Optional<TokenStorage> findByUsername(String username);
}
