package com.gbsw.messiofcoding.domain.auth.service;

import com.gbsw.messiofcoding.domain.auth.dto.request.LoginRequest;
import com.gbsw.messiofcoding.domain.auth.dto.request.RegisterRequest;
import com.gbsw.messiofcoding.domain.auth.dto.response.LoginServiceResult;
import com.gbsw.messiofcoding.domain.auth.dto.response.RegisterResponse;
import com.gbsw.messiofcoding.domain.auth.entity.RefreshToken;
import com.gbsw.messiofcoding.domain.auth.repository.RefreshTokenRepository;
import com.gbsw.messiofcoding.domain.users.entity.User;
import com.gbsw.messiofcoding.domain.users.repository.UserRepository;
import com.gbsw.messiofcoding.global.exception.CustomException;
import com.gbsw.messiofcoding.global.exception.ErrorCode;
import com.gbsw.messiofcoding.global.security.jwt.JwtProperties;
import java.time.LocalDateTime;
import com.gbsw.messiofcoding.global.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final JwtProperties jwtProperties;

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

    @Transactional
    public LoginServiceResult login(LoginRequest dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_CREDENTIALS));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_CREDENTIALS);
        }

        String accessToken = jwtProvider.generateAccessToken(user.getId());
        String refreshToken = jwtProvider.generateRefreshToken(user.getId());

        RefreshToken refreshTokenDB = RefreshToken.builder()
                .token(refreshToken)
                .expiryDate(LocalDateTime.now().plusSeconds(jwtProperties.getRefreshTokenExpiration() / 1000))
                .user(user)
                .build();

        refreshTokenRepository.save(refreshTokenDB);

        return new LoginServiceResult(accessToken, refreshToken);
    }
}