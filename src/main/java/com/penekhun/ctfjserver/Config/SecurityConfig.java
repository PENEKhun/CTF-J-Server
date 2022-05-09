package com.penekhun.ctfjserver.Config;

import com.penekhun.ctfjserver.Config.Jwt.JwtAccessDeniedHandler;
import com.penekhun.ctfjserver.Config.Jwt.JwtAuthenticationEntryPoint;
import com.penekhun.ctfjserver.Config.Jwt.JwtSecurityConfig;
import com.penekhun.ctfjserver.Config.Jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity //스프링 시큐리티 필터가 스프링 필터체인에 등록
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CorsFilter corsFilter;
    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

//    @Bean
//    @Override
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }

    @Bean //해당 메서드의 리턴되는 오브젝트를 IoC로 등록함
    public BCryptPasswordEncoder encodePwd(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //세션을 사용하지 않고, STATELESS모드로 사용
        .and()
            .addFilter(corsFilter) //cors 필터 적용 -> 필터에 의해 요청을 받음
            .formLogin().disable() //JWT form로그인 사용안함
            .httpBasic().disable() //기존적인 http로그인 방식을 사용안함
            //.addFilter(new JwtAuthenticationFilter(authenticationManager()))
            .authorizeRequests()
                .antMatchers("/api/v1/account/**")
                .access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/api/v1/admin/**")
                    .access("hasRole('ROLE_ADMIN')")
                .antMatchers("/api/v1/problem/**")
                    .access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")

                .antMatchers("/api/vi/login/**").hasAnyRole()
                .antMatchers("/api/vi/logout/**").hasAnyRole()

                .anyRequest().permitAll()

                .and()
                //addFilter메스드가 담긴 Config 클래스 추가
                .apply(new JwtSecurityConfig(tokenProvider));

    }

}
