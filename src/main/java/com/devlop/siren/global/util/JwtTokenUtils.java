package com.devlop.siren.global.util;

import com.devlop.siren.domain.user.dto.UserTokenDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
@Slf4j
public class JwtTokenUtils {
    public static String getUserEmail(String token, String secretKey){
        return extractClaims(token, secretKey).get("email", String.class);
    }
    public static boolean isExpired(String token, String secretKey){
        Date expiredDate = extractClaims(token, secretKey).getExpiration();
        return expiredDate.before(new Date());
    }
    private static Claims extractClaims(String token, String secretKey){
        return Jwts.parserBuilder().setSigningKey(getKey(secretKey))
                .build().parseClaimsJws(token).getBody();
    }
    public static UserTokenDto generateToken(String email, String secretKey, Long accessExpiredTimeMs, Long refreshExpiredTimeMs){
        Claims claims = Jwts.claims();
        claims.put("email", email);

        String accessToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + accessExpiredTimeMs))
                .signWith(getKey(secretKey), SignatureAlgorithm.HS256)
                .compact();

        String refreshToken = Jwts.builder()
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpiredTimeMs))
                .signWith(getKey(secretKey), SignatureAlgorithm.HS256)
                .compact();

        return UserTokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .refreshTokenExpiredMs(refreshExpiredTimeMs)
                .build();
    }
    private static Key getKey(String secretKey){
        byte[] keyByte = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyByte);
    }
}
