package com.devlop.siren.domain.user.service;

import com.devlop.siren.domain.user.domain.User;
import com.devlop.siren.domain.user.domain.UserRole;
import com.devlop.siren.domain.user.dto.request.UserRegisterRequest;
import com.devlop.siren.domain.user.repository.UserRepository;
import com.devlop.siren.domain.user.util.AllergyConverter;
import com.devlop.siren.global.exception.ErrorCode;
import com.devlop.siren.global.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final AllergyConverter converter;

    @Transactional
    public void register(UserRegisterRequest request) {
        userRepository.findByEmail(request.getEmail()).ifPresent(user -> {
            throw new GlobalException(ErrorCode.DUPLICATED_MEMBER);
        });

        User entity = UserRegisterRequest.fromDto(request, encoder.encode(request.getPassword()),
                UserRole.CUSTOMER, converter.convertToEntityAttribute(request.getAllergies()));

        userRepository.save(entity);
    }
}
