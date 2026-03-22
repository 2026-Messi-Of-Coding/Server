package com.gbsw.messiofcoding.domain.users.repository;

import com.gbsw.messiofcoding.domain.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByHandle(String handle);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
}