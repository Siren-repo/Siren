package com.devlop.siren.domain.user.service;

import com.devlop.siren.domain.item.utils.AllergyConverter;
import com.devlop.siren.domain.user.domain.User;
import com.devlop.siren.domain.user.domain.UserRole;
import com.devlop.siren.domain.user.dto.UserDetailsDto;
import com.devlop.siren.domain.user.dto.UserTokenDto;
import com.devlop.siren.domain.user.dto.request.UserLoginRequest;
import com.devlop.siren.domain.user.dto.request.UserRegisterRequest;
import com.devlop.siren.domain.user.dto.request.UserRoleChangeRequest;
import com.devlop.siren.domain.user.dto.response.UserReadResponse;
import com.devlop.siren.domain.user.repository.UserRepository;
import com.devlop.siren.global.common.response.ResponseCode;
import com.devlop.siren.global.exception.GlobalException;
import com.devlop.siren.global.util.JwtTokenUtils;
import com.devlop.siren.global.util.UserInformation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.time.Duration;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final AllergyConverter converter;
    private final RedisService redisService;
    private final JwtTokenUtils utils;

    @Value("${jwt.secret-key}")
    private String secretKey;
    @Value("${jwt.access-token.expired-time-ms}")
    private Long accessExpiredTimeMs;
    @Value("${jwt.refresh-token.expired-time-ms}")
    private Long refreshExpiredTimeMs;

    public UserDetailsDto loadMemberByEmail(String email) {
        User registeredUser = userRepository.findByEmail(email).orElseThrow(() ->
                new GlobalException(ResponseCode.ErrorCode.NOT_FOUND_MEMBER));
        return UserDetailsDto.fromEntity(registeredUser);
    }

    @Transactional
    public UserReadResponse register(UserRegisterRequest request) {
        userRepository.findByEmail(request.getEmail()).ifPresent(user -> {
            throw new GlobalException(ResponseCode.ErrorCode.DUPLICATED_MEMBER);
        });

        User entity = UserRegisterRequest.fromDto(request, encoder.encode(request.getPassword()),
                UserRole.CUSTOMER, converter.convertToEntityAttribute(request.getAllergies()));

        return UserReadResponse.of(userRepository.save(entity));
    }

    @Transactional
    public UserTokenDto login(UserLoginRequest request, HttpServletResponse response) {
        User savedUser = userRepository.findByEmail(request.getEmail()).orElseThrow(() ->
                new GlobalException(ResponseCode.ErrorCode.NOT_FOUND_MEMBER));

        checkPassword(request.getPassword(),savedUser.getPassword());
        checkSavedRefreshTokenInRedis(savedUser.getEmail());

        UserTokenDto token = generateToken(savedUser.getEmail());
        utils.setAccessTokenInHeader(token.getAccessToken(), response);
        utils.setRefreshTokenInHeader(token.getRefreshToken(), response);

        return token;
    }

    public void logout(UserTokenDto tokenDto){
        String requestUserEmail = utils.extractClaims(tokenDto.getRefreshToken()).getSubject();
        checkRefreshTokenInRedis(requestUserEmail);

        redisService.deleteValue(requestUserEmail);
        redisService.setValue(tokenDto.getAccessToken(), "logout", Duration.ofMillis(accessExpiredTimeMs));
    }

    public String reissueAccessToken(String refreshToken, HttpServletResponse response){
        String requestEmail = utils.extractClaims(refreshToken).getSubject();
        checkRefreshTokenInRedis(requestEmail);

        UserDetailsDto userDetail = loadMemberByEmail(requestEmail);
        String newAccessToken = utils.generateAccessToken(userDetail.getEmail(), secretKey, accessExpiredTimeMs);
        utils.setAccessTokenInHeader(newAccessToken, response);
        return newAccessToken;
    }

    @Transactional
    public UserReadResponse changeRole(UserRoleChangeRequest request, UserDetailsDto requestUser){
        UserInformation.validAdmin(requestUser);
        User user = userRepository.findByEmail(request.getUserEmail()).orElseThrow(() ->
                new GlobalException(ResponseCode.ErrorCode.NOT_FOUND_MEMBER));
        user.changeRole(UserRole.valueOf(request.getRoleType()));
        return UserReadResponse.of(user);
    }

    private void checkPassword(String request, String password){
        if (!encoder.matches(request, password))
            throw new GlobalException(ResponseCode.ErrorCode.INVALID_PASSWORD);
    }
    private UserTokenDto generateToken(String requestEmail){
        UserTokenDto tokenDto = UserTokenDto.builder()
                .accessToken(utils.generateAccessToken(requestEmail, secretKey, accessExpiredTimeMs))
                .refreshToken(utils.generateRefreshToken(requestEmail, secretKey, refreshExpiredTimeMs))
                .build();

        redisService.setValue(requestEmail, tokenDto.getRefreshToken(), Duration.ofMillis(refreshExpiredTimeMs));
        return new UserTokenDto(tokenDto.getAccessToken(), tokenDto.getRefreshToken());
    }
    private void checkRefreshTokenInRedis(String email){
        if(!redisService.existRefreshToken(email))
            throw new GlobalException(ResponseCode.ErrorCode.EXPIRED_REFRESH_TOKEN);
    }
    private void checkSavedRefreshTokenInRedis(String email){
        if(redisService.existRefreshToken(email))
            throw new GlobalException(ResponseCode.ErrorCode.ALREADY_LOGGED_IN);
    }
}