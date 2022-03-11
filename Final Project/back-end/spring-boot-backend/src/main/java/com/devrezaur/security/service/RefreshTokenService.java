package com.devrezaur.security.service;

import com.devrezaur.repository.UserRepository;
import com.devrezaur.security.models.RefreshToken;
import com.devrezaur.security.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Value("${jwt.refreshExp}")
    private Long refreshTokenDurationMs;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private UserRepository userRepository;

    public RefreshToken findByRefreshToken(String token) {
        return refreshTokenRepository.findByRefreshToken(token);
    }

    public RefreshToken createRefreshToken(String userId) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(userRepository.findById(userId).get());
        refreshToken.setExpDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setRefreshToken(UUID.randomUUID().toString());
        refreshToken = refreshTokenRepository.save(refreshToken);

        return refreshToken;
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            return null;
        }

        return token;
    }

    @Transactional
    public int deleteByUserId(String userId) throws Exception {
        try {
            return refreshTokenRepository.deleteByUser(userRepository.findById(userId).get());
        } catch (Exception ex) {
            throw new Exception("No Session Found!");
        }
    }

}

