package com.penekhun.ctfjserver.User.Entity;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@RedisHash("TokenStorage")
@Builder
@Getter
@ToString
public class TokenStorage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TOKEN_ID", nullable = false)
    private Long refreshTokenId;

    @Column(name = "REFRESH_TOKEN", nullable = false)
    private String refreshToken;

    @Column(name = "ACCESS_TOKEN", nullable = false)
    private String accessToken;

    @Column(name = "USERNAME", nullable = false)
    private String username;

    @TimeToLive
    private Long expiration;

    public void setExpiration(Long expiration) {
        this.expiration = expiration;
    }


    //
//    @Column(name = "Expired", nullable = false)
//    private Date expired;
//
//    @Column(name ="USER_AGENT", nullable = false)
//    private String userAgent;
}
