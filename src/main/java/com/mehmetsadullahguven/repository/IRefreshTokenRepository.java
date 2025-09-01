package com.mehmetsadullahguven.repository;

import com.mehmetsadullahguven.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByUserId(Long userId);

    @Query("from RefreshToken r WHERE r.refreshToken = :refreshToken")
    Optional<RefreshToken> findByRefreshToken(String refreshToken);
}
