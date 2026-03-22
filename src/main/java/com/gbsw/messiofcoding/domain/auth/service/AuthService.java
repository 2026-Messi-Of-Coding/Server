package com.gbsw.messiofcoding.domain.auth.service;

import com.gbsw.messiofcoding.domain.auth.dto.request.RegisterRequest;
import com.gbsw.messiofcoding.domain.auth.dto.response.RegisterResponse;
import com.gbsw.messiofcoding.domain.users.entity.User;
import com.gbsw.messiofcoding.domain.users.repository.UserRepository;
import com.gbsw.messiofcoding.global.exception.CustomException;
import com.gbsw.messiofcoding.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public RegisterResponse register(RegisterRequest dto) {
        if (userRepository.existsByHandle(dto.getHandle())) {
            throw new CustomException(ErrorCode.DUPLICATE_HANDLE);
        }
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }
        if (userRepository.existsByPhone(dto.getPhone())) {
            throw new CustomException(ErrorCode.DUPLICATE_PHONE);
        }

        User user = User.builder()
                .handle(dto.getHandle())
                .username(dto.getUsername())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .password(passwordEncoder.encode(dto.getPassword()))
                .build();

        userRepository.save(user);

        return RegisterResponse.from(user);
    }
}