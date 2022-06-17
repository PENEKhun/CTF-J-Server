package com.penekhun.ctfjserver.Config;

import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Parameter(hidden = true)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
@AuthenticationPrincipal(expression = "#this == 'anonymousUser' ? null : account")
public @interface CurrentUserParameter {
}


