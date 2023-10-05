package com.devlop.siren.domain.user.service;

import com.devlop.siren.domain.user.domain.User;
import com.devlop.siren.domain.user.domain.UserRole;
import com.devlop.siren.domain.user.dto.UserDetailsDto;
import com.devlop.siren.domain.user.dto.UserTokenDto;
import com.devlop.siren.domain.user.dto.request.UserLoginRequest;
import com.devlop.siren.domain.user.dto.request.UserRegisterRequest;
import com.devlop.siren.domain.user.dto.response.UserLoginResponse;
import com.devlop.siren.domain.user.repository.UserRepository;
import com.devlop.siren.domain.user.util.AllergyConverter;
import com.devlop.siren.global.exception.ErrorCode;
import com.devlop.siren.global.exception.GlobalException;
import com.devlop.siren.global.util.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final RedisService redisService;
    private final BCryptPasswordEncoder encoder;
    private final AllergyConverter converter;

    @Value("${jwt.secret-key}")
    private String secretKey;
    @Value("${jwt.access-token.expired-time-ms}")
    private Long accessExpiredTimeMs;
    @Value("${jwt.refresh-token.expired-time-ms}")
    private Long refreshExpiredTimeMs;

    public UserDetailsDto loadMemberByEmail(String email) {
        User registeredUser = userRepository.findByEmail(email).orElseThrow(() ->
                new GlobalException(ErrorCode.NOT_FOUND_MEMBER));
        registeredUser.isDeleted();
        return UserDetailsDto.fromEntity(registeredUser);
    }

    @Transactional
    public void register(UserRegisterRequest request) {
        userRepository.findByEmail(request.getEmail()).ifPresent(user -> {
            throw new GlobalException(ErrorCode.DUPLICATED_MEMBER);
        });

        User entity = UserRegisterRequest.fromDto(request, encoder.encode(request.getPassword()),
                UserRole.CUSTOMER, converter.convertToEntityAttribute(request.getAllergies()));

        userRepository.save(entity);
    }

    @Transactional
    public UserLoginResponse login(UserLoginRequest request){
        User savedUser = userRepository.findByEmail(request.getEmail()).orElseThrow( () ->
                new GlobalException(ErrorCode.NOT_FOUND_MEMBER));

        if(!encoder.matches(request.getPassword(), savedUser.getPassword()))
            throw new GlobalException(ErrorCode.INVALID_PASSWORD);

        UserTokenDto tokenDto = JwtTokenUtils.generateToken(request.getEmail(), secretKey, accessExpiredTimeMs, refreshExpiredTimeMs);
        redisService.setValue(savedUser.getEmail(), tokenDto.getRefreshToken(), Duration.ofMillis(tokenDto.getRefreshTokenExpiredMs()));

        return new UserLoginResponse(tokenDto.getAccessToken(), tokenDto.getRefreshToken());
    }
}
