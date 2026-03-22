package com.gbsw.messiofcoding.domain.auth.controller;

import com.gbsw.messiofcoding.domain.auth.dto.request.LoginRequest;
import com.gbsw.messiofcoding.domain.auth.dto.request.RegisterRequest;
import com.gbsw.messiofcoding.domain.auth.dto.response.LoginResponse;
import com.gbsw.messiofcoding.domain.auth.dto.response.LoginServiceResult;
import com.gbsw.messiofcoding.domain.auth.dto.response.RegisterResponse;
import com.gbsw.messiofcoding.domain.auth.service.AuthService;
import com.gbsw.messiofcoding.global.security.jwt.JwtProperties;
import com.gbsw.messiofcoding.global.common.ApiResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final JwtProperties jwtProperties;

    @PostMapping("register")
    public ResponseEntity<ApiResponse<RegisterResponse>> register(
            @Valid @RequestBody RegisterRequest dto
    ) {
        RegisterResponse rs = authService.register(dto);

        return ResponseEntity.ok(ApiResponse.success(rs));
    }

    @PostMapping("login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest dto,
            HttpServletResponse response
    ) {
        LoginServiceResult result = authService.login(dto);

        Cookie cookie = new Cookie("refreshToken", result.refreshToken());
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge((int) (jwtProperties.getRefreshTokenExpiration() / 1000));
        response.addCookie(cookie);

        return ResponseEntity.ok(ApiResponse.success(new LoginResponse(result.accessToken())));
    }
}
