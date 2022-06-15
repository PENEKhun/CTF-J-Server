package com.penekhun.ctfjserver.User.Dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenDto {
    @ApiModelProperty(value = "access token")
    private String token;
    @ApiModelProperty(value = "token expired")
    private String tokenExpired;
    @ApiModelProperty(value = "refresh token")
    private String refresh;
}