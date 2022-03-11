package com.devrezaur.security.jwt;

import com.devrezaur.security.models.UserDetailsImplementation;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Value("${jwt.jwtExp}")
    private int JWT_EXP;

    public String generateToken(UserDetailsImplementation userDetailsImplementation) {
        List<String> roles = userDetailsImplementation.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        Map<String, Object> claims = new HashMap<>();
        claims.put("ROLES", roles);

        return createToken(claims, userDetailsImplementation.getUsername());
    }

    public String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date((new Date()).getTime()))
                .setExpiration(new Date((new Date()).getTime() + JWT_EXP))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public boolean validateJwtToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
            logger.error("Invalid token");
        } catch (ExpiredJwtException ex) {
            logger.error("Expired token");
        }

        return false;
    }

    public String extractUsername(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
    }

    @SuppressWarnings("unchecked")
    public List<String> extractRoles(String token) {
        return (List<String>) extractClaim(token).get("ROLES");
    }

    public Claims extractClaim(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

}
