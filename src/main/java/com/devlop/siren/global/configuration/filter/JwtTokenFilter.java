package com.devlop.siren.global.configuration.filter;

import com.devlop.siren.domain.user.dto.UserDetailsDto;
import com.devlop.siren.domain.user.service.RedisService;
import com.devlop.siren.domain.user.service.UserService;
import com.devlop.siren.global.common.response.ApiResponse;
import com.devlop.siren.global.common.response.ResponseCode;
import com.devlop.siren.global.exception.GlobalException;
import com.devlop.siren.global.util.JwtTokenUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    private final String AUTHORIZATION_HEADER = "Authorization";

    private final UserService userService;
    private final RedisService redisService;

    private final static List<String> PERMIT_URLS = List.of(
            "/",
            "/h2",
            "/api/users",
            "/api/users/sessions",
            "/api/users/sessions/renew");

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return PERMIT_URLS.stream().anyMatch(exclude -> exclude.equalsIgnoreCase(request.getServletPath()));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            final String header = request.getHeader(AUTHORIZATION_HEADER);
            if(header == null || !header.startsWith("Bearer ")){
                log.error("Authorization Header does not start with Bearer {}", request.getRequestURI());
                throw new GlobalException(ResponseCode.ErrorCode.INVALID_TOKEN);
            }

            final String token = header.split(" ")[1].trim();
            if (JwtTokenUtils.isExpired(token)) {
                throw new GlobalException(ResponseCode.ErrorCode.EXPIRED_ACCESS_TOKEN);
            }

            if (redisService.existAccessToken(token)) {
                throw new GlobalException(ResponseCode.ErrorCode.INVALID_TOKEN);
            }

            String email = JwtTokenUtils.getUserEmail(token);
            UserDetailsDto user = userService.loadMemberByEmail(email);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    user, null, user.getAuthorities()
            );

            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            log.info("Success token verification");

        } catch (RuntimeException e) {
            if(e instanceof GlobalException) {
                ObjectMapper objectMapper = new ObjectMapper();
                String json = objectMapper.writeValueAsString(ApiResponse.error(((GlobalException) e).getErrorCode()));
                response.getWriter().write(json);
                response.setStatus(((GlobalException) e).getErrorCode().getStatus().value());
            }
        }
        filterChain.doFilter(request, response);
    }

}
