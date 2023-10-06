package com.devlop.siren.global.configuration.filter;

import com.devlop.siren.domain.user.dto.UserDetailsDto;
import com.devlop.siren.domain.user.service.RedisService;
import com.devlop.siren.domain.user.service.UserService;
import com.devlop.siren.global.exception.ErrorCode;
import com.devlop.siren.global.exception.GlobalException;
import com.devlop.siren.global.util.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {

    private final String secretKey;
    private final UserService userService;
    private final RedisService redisService;

    private final static List<String> PERMIT_URLS = List.of(
            "/",
            "/h2",
            "/api/users/register",
            "/api/users/login",
            "/api/users/reissue");

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return PERMIT_URLS.stream().anyMatch(exclude -> exclude.equalsIgnoreCase(request.getServletPath()));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(header == null || !header.startsWith("Bearer ")){
            log.error("Authorization Header does not start with Bearer {}", request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }

        final String token = header.split(" ")[1].trim();
        if(JwtTokenUtils.isExpired(token, secretKey)){
            throw new GlobalException(ErrorCode.EXPIRED_TOKEN);
        }
        // TODO :: 로그아웃 처리한 token 인지 확인

        String email = JwtTokenUtils.getUserEmail(token, secretKey);
        UserDetailsDto user = userService.loadMemberByEmail(email);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
          user, null, user.getAuthorities()
        );

        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        log.info("Success token verification");

        filterChain.doFilter(request, response);
    }

}
