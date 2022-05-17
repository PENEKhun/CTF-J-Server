package com.penekhun.ctfjserver.User.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenDto {
    private String token;
    private String tokenExpired;
    private String refresh;
}