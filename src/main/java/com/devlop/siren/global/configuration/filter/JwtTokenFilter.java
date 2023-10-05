package com.devlop.siren.global.configuration.filter;

import com.devlop.siren.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final String secretKey;
    private final UserService userService;

    private final static List<String> PERMIT_URLS = List.of("/api/users/register", "/api/users/login");
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(PERMIT_URLS.contains(request.getRequestURI())){
            filterChain.doFilter(request, response);
            return;
        }
        //TODO :: authentication urls 처리
    }
}
