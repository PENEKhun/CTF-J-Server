package com.penekhun.ctfjserver.User.Entity;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@RedisHash("RefreshToken")
@Builder
@Getter
@ToString
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REFRESH_TOKEN_ID", nullable = false)
    private Long refreshTokenId;

    @Column(name = "REFRESH_TOKEN", nullable = false)
    private String refreshToken;

    @Column(name = "KEY_USERNAME", nullable = false)
    private String keyUsername;
//
//    @Column(name = "Expired", nullable = false)
//    private Date expired;
//
//    @Column(name ="USER_AGENT", nullable = false)
//    private String userAgent;
}
