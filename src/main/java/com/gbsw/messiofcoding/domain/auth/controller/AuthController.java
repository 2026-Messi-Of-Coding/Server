package com.gbsw.messiofcoding.domain.auth.controller;

import com.gbsw.messiofcoding.domain.auth.dto.request.RegisterRequest;
import com.gbsw.messiofcoding.domain.auth.dto.response.RegisterResponse;
import com.gbsw.messiofcoding.domain.auth.service.AuthService;
import com.gbsw.messiofcoding.global.common.ApiResponse;
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

    @PostMapping("register")
    public ResponseEntity<ApiResponse<RegisterResponse>> register(
            @Valid @RequestBody RegisterRequest dto
    ) {
        RegisterResponse rs = authService.register(dto);

        return ResponseEntity.ok(ApiResponse.success(rs));
    }
}
