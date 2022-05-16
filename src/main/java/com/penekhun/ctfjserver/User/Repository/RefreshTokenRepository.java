package com.penekhun.ctfjserver.User.Repository;

import com.penekhun.ctfjserver.User.Entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {
}
