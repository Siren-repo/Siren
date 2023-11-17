package com.devlop.siren.global.util;

import com.devlop.siren.domain.user.dto.UserTokenDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
public class JwtTokenUtils {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String REFRESH_HEADER = "Refresh";
    private static String secretKey;

    @Value("${jwt.secret-key}")
    public void setSecretKey(String secretKey) {
        JwtTokenUtils.secretKey = secretKey;
    }

    public static String generateAccessToken(String email, String secretKey, Long accessExpiredTimeMs) {
        Claims claims = Jwts.claims();
        claims.put("email", email);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + accessExpiredTimeMs))
                .signWith(getKey(secretKey), SignatureAlgorithm.HS256)
                .compact();
    }

    public static String generateRefreshToken(String email, String secretKey, Long refreshExpiredTimeMs) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpiredTimeMs))
                .signWith(getKey(secretKey), SignatureAlgorithm.HS256)
                .compact();
    }

    public UserTokenDto resolveToken(HttpServletRequest request) {
        String access = request.getHeader(AUTHORIZATION_HEADER);
        String refresh = request.getHeader(REFRESH_HEADER);
        if (StringUtils.hasText(access) && StringUtils.hasText(refresh) && access.startsWith("Bearer ")) {
            return UserTokenDto.builder()
                    .accessToken(access.split(" ")[1].trim())
                    .refreshToken(refresh)
                    .build();
        }
        return null;
    }

    public static void setAccessTokenInHeader(String accessToken, HttpServletResponse response) {
        response.setHeader(AUTHORIZATION_HEADER, "Bearer " + accessToken);
    }

    public static void setRefreshTokenInHeader(String refreshToken, HttpServletResponse response) {
        response.setHeader(REFRESH_HEADER, refreshToken);
    }

    public static String getUserEmail(String token) {
        return extractClaims(token).get("email", String.class);
    }

    public static boolean isExpired(String token) {
        Date expiredDate = extractClaims(token).getExpiration();
        return expiredDate.before(new Date());
    }

    public static Claims extractClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getKey(secretKey))
                .build().parseClaimsJws(token).getBody();
    }

    public static Key getKey(String secretKey) {
        byte[] keyByte = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyByte);
    }
}
