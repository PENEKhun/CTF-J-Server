package com.penekhun.ctfjserver.User.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenDto {
    @Schema(description = "access token")
    private String token;
    @Schema(description = "token expired")
    private String tokenExpired;
    @Schema(description = "refresh token")
    private String refresh;
}