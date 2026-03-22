package com.gbsw.messiofcoding.global.security.service;

import com.gbsw.messiofcoding.domain.users.entity.User;
import com.gbsw.messiofcoding.domain.users.repository.UserRepository;
import com.gbsw.messiofcoding.global.exception.CustomException;
import com.gbsw.messiofcoding.global.exception.ErrorCode;
import com.gbsw.messiofcoding.global.security.CustomUserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

     private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
         User user = userRepository.findById(Long.valueOf(userId))
                 .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
         return new CustomUserPrincipal(user.getId(), user.getUsername(), user.getPassword());
    }
}