package com.devrezaur.security.repository;

import com.devrezaur.model.User;
import com.devrezaur.security.models.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    RefreshToken findByRefreshToken(String token);

    @Modifying
    int deleteByUser(User user);
}
