package com.devlop.siren.global.configuration;

import com.devlop.siren.domain.user.service.RedisService;
import com.devlop.siren.domain.user.service.UserService;
import com.devlop.siren.global.configuration.filter.JwtTokenFilter;
import com.devlop.siren.global.exception.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@EnableWebSecurity
public class AuthenticationConfig {

    private final UserService userService;
    private final RedisService redisService;

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors().and().csrf().disable()
                .authorizeRequests((authorizeRequests) ->
                        authorizeRequests
                                .antMatchers("/", "/auth/**", "/api/users",
                                        "/api/users/sessions", "/api/users/sessions/renew").permitAll()
                                .antMatchers("/api/**").authenticated()
                )
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(new JwtTokenFilter(userService, redisService),
                        UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling((exceptionHandling) ->
                        exceptionHandling.authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                );

        return http.build();
    }
}
