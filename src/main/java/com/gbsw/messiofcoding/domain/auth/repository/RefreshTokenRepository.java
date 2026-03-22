package com.gbsw.messiofcoding.domain.auth.repository;

import com.gbsw.messiofcoding.domain.auth.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
}
