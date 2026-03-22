package com.gbsw.messiofcoding.domain.auth.dto.response;

import com.gbsw.messiofcoding.domain.users.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RegisterResponse {
    private String handle;
    private String username;
    private String email;

    public static RegisterResponse from(User user) {
        return RegisterResponse.builder()
                .handle(user.getHandle())
                .username(user.getUsername())
                .build();
    }
}